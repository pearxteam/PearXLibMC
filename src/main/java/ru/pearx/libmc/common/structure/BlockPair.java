package ru.pearx.libmc.common.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

/*
 * Created by mrAppleXZ on 20.11.17 18:17.
 */
public class BlockPair
{
    private IBlockState state;
    private TileEntity tile;

    public BlockPair(IBlockState state, TileEntity tile)
    {
        this.state = state;
        this.tile = tile;
    }

    public IBlockState getState()
    {
        return state;
    }

    public void setState(IBlockState state)
    {
        this.state = state;
    }

    public TileEntity getTile()
    {
        return tile;
    }

    public void setTile(TileEntity tile)
    {
        this.tile = tile;
    }
}
