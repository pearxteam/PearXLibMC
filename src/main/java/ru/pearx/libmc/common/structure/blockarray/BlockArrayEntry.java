package ru.pearx.libmc.common.structure.blockarray;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/*
 * Created by mrAppleXZ on 15.10.17 17:07.
 */
public class BlockArrayEntry
{
    private TileEntity tile;
   // @SideOnly(Side.CLIENT)
    //private TileEntitySpecialRenderer tesr;
    private IBlockState state;
    private ItemStack stack;

    public BlockArrayEntry(IBlockState state, ItemStack stack, TileEntity tile)
    {
        setTile(tile);
        this.state = state;
        this.stack = stack;
    }

    public BlockArrayEntry(IBlockState state, ItemStack stack)
    {
        this.state = state;
        this.stack = stack;
    }

    public BlockArrayEntry(Block block, ItemStack stack, TileEntity tile)
    {
        this(block.getDefaultState(), stack, tile);
    }

    public BlockArrayEntry(Block block, ItemStack stack)
    {
        this(block.getDefaultState(), stack);
    }

    public TileEntity getTile()
    {
        return tile;
    }

    public void setTile(TileEntity tile)
    {
        this.tile = tile;
        /*if(tile != null && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            //noinspection MethodCallSideOnly,VariableUseSideOnly
            tesr = TileEntityRendererDispatcher.instance.getRenderer(tile.getClass());
        }*/
    }

    /*@SideOnly(Side.CLIENT)
    public TileEntitySpecialRenderer getTESR()
    {
        return tesr;
    }*/

    public IBlockState getState()
    {
        return state;
    }

    public void setState(IBlockState state)
    {
        this.state = state;
    }

    public ItemStack getStack()
    {
        return stack;
    }

    public void setStack(ItemStack stack)
    {
        this.stack = stack;
    }
}
