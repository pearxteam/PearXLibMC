package ru.pearx.libmc.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockActivatedEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockBreakEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockGetFaceShapeEvent;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockPickBlockEvent;
import ru.pearx.libmc.common.tiles.TileMultiblockSlave;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 19.11.17 16:02.
 */
public class BlockMultiblockPart extends BlockBase
{
    public BlockMultiblockPart(Material materialIn)
    {
        super(materialIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        Multiblock.sendEventToMaster(worldIn, pos, new MultiblockBreakEvent(state), null);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return Multiblock.sendEventToMaster(worldIn, pos, new MultiblockActivatedEvent(state, playerIn, hand, facing, hitX, hitY, hitZ), false);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return Multiblock.sendEventToMaster(world, pos, new MultiblockPickBlockEvent(state, target, player), ItemStack.EMPTY);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
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

    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess w, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        return Multiblock.sendEventToMaster(w, pos, new MultiblockGetFaceShapeEvent(state, facing), BlockFaceShape.UNDEFINED);
    }
}
