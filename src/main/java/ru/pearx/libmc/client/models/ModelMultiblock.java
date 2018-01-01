package ru.pearx.libmc.client.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.ModelSupplied;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by mrAppleXZ on 24.12.17 10:24.
 */
@SideOnly(Side.CLIENT)
public class ModelMultiblock extends PXModel
{
    private Map<String, ModelSupplied> baked = new HashMap<>();

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if(state == null && getStack() != null && getStack().hasTagCompound() && getStack().getTagCompound().hasKey("multiblock", Constants.NBT.TAG_STRING))
        {
            String id = getStack().getTagCompound().getString("multiblock");
            if(!baked.containsKey(id))
                baked.put(id, new ModelSupplied(Multiblock.REGISTRY.getValue(new ResourceLocation(id)).getItemModel()));
            return baked.get(id).get().getQuads(state, side, rand);
        }
        return DUMMY_LIST;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return null;
    }

    @Override
    public void bake()
    {

    }
}
