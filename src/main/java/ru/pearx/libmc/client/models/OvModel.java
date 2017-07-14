package ru.pearx.libmc.client.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.models.processors.IQuadProcessor;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrAppleXZ on 10.04.17 8:55.
 */
@SideOnly(Side.CLIENT)
public class OvModel implements IPXModel
{
    protected List<IQuadProcessor> processors = new ArrayList<>();
    private ResourceLocation baseModel;
    private IBakedModel baked;
    private WeakReference<ItemStack> stack;
    private boolean flipV = true;

    @Override
    public void bake()
    {
        IModel mdl;
        try
        {
            mdl = ModelLoaderRegistry.getModel(getBaseModel());
            if(flipV)
            {
                mdl = mdl.process(ImmutableMap.of("flip-v", "true"));
            }
            baked = mdl.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        List<BakedQuad> l = new ArrayList<>();
        for (BakedQuad quad : getBaked().getQuads(state, side, rand))
            l.add(new BakedQuad(quad.getVertexData(), 1, quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
        for(IQuadProcessor proc : processors)
            proc.process(l, state, side, rand);
        return l;
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
        return getBaked().getOverrides();
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
        if(stack != null)
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
}
