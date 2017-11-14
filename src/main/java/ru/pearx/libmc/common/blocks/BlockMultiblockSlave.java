package ru.pearx.libmc.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMaster;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockSlave;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockBreakEvent;
import ru.pearx.libmc.common.tiles.TileMultiblockSlave;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 13.11.17 17:42.
 */
public class BlockMultiblockSlave extends BlockBase
{
    public BlockMultiblockSlave()
    {
        super(Material.ROCK);
        setRegistryName("multiblock_slave");
    }

    @Override
    public Material getMaterial(IBlockState state)
    {
        //todo
        return super.getMaterial(state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        sendEventToMaster(new MultiblockBreakEvent(worldIn, pos, state));
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileMultiblockSlave();
    }

    public void sendEventToMaster(IMultiblockEvent evt)
    {
        TileEntity te = evt.getWorld().getTileEntity(evt.getPos());
        if(te != null && te instanceof IMultiblockSlave)
        {
            TileEntity master = te.getWorld().getTileEntity(((IMultiblockSlave) te).getMasterPos());
            if(master != null && master instanceof IMultiblockMaster)
            {
                ((IMultiblockMaster) master).handleEvent(evt);
            }
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        //leave this empty.
    }
}
