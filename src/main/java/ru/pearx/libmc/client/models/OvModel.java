package ru.pearx.libmc.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.models.processors.IQuadProcessor;
import ru.pearx.libmc.client.models.processors.IVertexProcessor;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mrAppleXZ on 10.04.17 8:55.
 */
@SideOnly(Side.CLIENT)
public class OvModel implements IPXModel
{
    public static Field unpQuadData;

    static
    {
        try
        {
            unpQuadData = UnpackedBakedQuad.class.getDeclaredField("unpackedData");
            unpQuadData.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            PXLMC.getLog().error("Can't locate the 'unpackedData' field!");
        }
    }

    protected List<IQuadProcessor> quadProcessors = new ArrayList<>();
    protected List<IVertexProcessor> vertexProcessors = new ArrayList<>();
    private ResourceLocation baseModel;
    private IBakedModel baked;
    private WeakReference<ItemStack> stack;
    private boolean flipV = true;
    private boolean disableSides = true;

    protected static final ImmutableList<BakedQuad> DUMMY_LIST = ImmutableList.of();
    public static final ItemOverrideList OVERRIDE_LIST = new ItemOverrideList(Collections.emptyList())
    {
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            if (originalModel instanceof IPXModel)
                ((IPXModel) originalModel).setStack(stack);
            return originalModel;
        }
    };

    @Override
    public void bake()
    {
        IModel mdl;
        try
        {
            mdl = ModelLoaderRegistry.getModel(getBaseModel());
            if (flipV)
            {
                mdl = mdl.process(ImmutableMap.of("flip-v", "true"));
            }
            baked = mdl.bake(getModelState(this, mdl), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if (shouldDisableSides() && side != null)
            return DUMMY_LIST;

        List<BakedQuad> l = new ArrayList<>();
        l.addAll(getBaked().getQuads(state, side, rand));
        process(l, state, side, rand);
        return l;
    }

    protected void process(List<BakedQuad> quads, @Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        for (IQuadProcessor proc : quadProcessors)
            if ((proc.processState() && state != null) || (proc.processStack() && state == null))
                proc.process(quads, state, side, rand, this);
        List<IVertexProcessor> validVertexProcs = vertexProcessors.parallelStream()
                .filter(proc -> (proc.processState() && state != null) || (proc.processStack() && state == null))
                .collect(Collectors.toList());
        if (!validVertexProcs.isEmpty())
        {
            for (IVertexProcessor proc : validVertexProcs)
                proc.preProcess(quads, state, side, rand, this);
            List<BakedQuad> processed = quads.parallelStream().map(q ->
            {
                UnpackedBakedQuad.Builder bld = new UnpackedBakedQuad.Builder(q.getFormat());
                bld.setQuadTint(q.getTintIndex());
                bld.setQuadOrientation(q.getFace());
                bld.setTexture(q.getSprite());
                bld.setApplyDiffuseLighting(q.shouldApplyDiffuseLighting());
                for (IVertexProcessor proc : validVertexProcs)
                    proc.processQuad(bld, q, state, side, rand, this);
                if (q instanceof UnpackedBakedQuad)
                {
                    try
                    {
                        float[][][] data = (float[][][]) unpQuadData.get(q);
                        for (int i = 0; i < 4; i++)
                        {
                            for (int e = 0; e < q.getFormat().getElementCount(); e++)
                            {
                                float[] lst = Arrays.copyOf(data[i][e], data[i][e].length);
                                for (IVertexProcessor proc : validVertexProcs)
                                    lst = proc.processVertex(bld, q, lst, i, e, state, side, rand, this);
                                bld.put(e, lst);
                            }
                        }

                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    for (int i = 0; i < 4; i++)
                    {
                        for (int e = 0; e < q.getFormat().getElementCount(); e++)
                        {
                            float[] lst = new float[q.getFormat().getElement(e).getElementCount()];
                            LightUtil.unpack(q.getVertexData(), lst, q.getFormat(), i, e);
                            for (IVertexProcessor proc : validVertexProcs)
                                lst = proc.processVertex(bld, q, lst, i, e, state, side, rand, this);
                            bld.put(e, lst);
                        }
                    }
                }
                return bld.build();
            }).collect(Collectors.toList());
            quads.clear();
            quads.addAll(processed);
        }
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return getBaked().isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return getBaked().isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return getBaked().isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return getBaked().getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return getBaked().getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return OVERRIDE_LIST;
    }

    public boolean shouldFlipV()
    {
        return flipV;
    }

    public void setShouldFlipV(boolean flipV)
    {
        this.flipV = flipV;
    }

    @Override
    public void setStack(ItemStack stack)
    {
        this.stack = new WeakReference<>(stack);
    }

    @Override
    public ItemStack getStack()
    {
        if (stack != null)
            return stack.get();
        return null;
    }

    protected IBakedModel getBaked()
    {
        return baked;
    }

    protected void setBaked(IBakedModel baked)
    {
        this.baked = baked;
    }

    public void setBaseModel(ResourceLocation loc)
    {
        baseModel = loc;
    }

    public ResourceLocation getBaseModel()
    {
        return baseModel;
    }

    public boolean shouldDisableSides()
    {
        return disableSides;
    }

    public void setShouldDisableSides(boolean disableSides)
    {
        this.disableSides = disableSides;
    }

    public IModelState getModelState(IPXModel th, IModel model)
    {
        return TRSRTransformation.identity();
    }
}
