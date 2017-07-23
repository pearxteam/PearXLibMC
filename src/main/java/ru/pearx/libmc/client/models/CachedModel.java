package ru.pearx.libmc.client.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by mrAppleXZ on 22.07.17 19:08.
 */
public class CachedModel extends OvModel
{
    private Map<EnumFacing, List<BakedQuad>> cache = new HashMap<>();
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        if(!isCacheActual(state, side, rand))
            updateCache(state, side, rand);
        return cache.get(side);
    }

    public boolean isCacheActual(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return cache.containsKey(side) && cache.get(side) != null;
    }

    public void updateCache(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        cache.put(side, super.getQuads(state, side, rand));
    }
}
