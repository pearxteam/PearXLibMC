package ru.pearx.libmc.common.blocks;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
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
public abstract class BlockMultiblockPart extends BlockBase
{
    public enum Type implements IStringSerializable
    {
        MASTER,
        SLAVE;

        @Override
        public String getName()
        {
            return toString().toLowerCase();
        }
    }

    public static PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockMultiblockPart(Material materialIn)
    {
        super(materialIn);
        setDefaultState(getDefaultState().withProperty(TYPE, Type.MASTER));
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

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(TYPE, Type.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPE).ordinal();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        switch (state.getValue(TYPE))
        {
            case MASTER:
                return createMasterTile(world, state);
            case SLAVE:
                return createSlaveTile(world, state);
        }
        return null;
    }

    @Override
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return super.getPackedLightmapCoords(state, source, pos);
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.BLOCK;
    }

    public abstract TileEntity createMasterTile(World world, IBlockState state);
    public TileEntity createSlaveTile(World world, IBlockState state)
    {
        return new TileMultiblockSlave();
    }
}
