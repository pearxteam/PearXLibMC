package ru.pearx.libmc.common.blocks.controllers;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

/**
 * Created by mrAppleXZ on 04.06.17 11:34.
 */
public class HorizontalFacingController
{
    public static final PropertyDirection FACING_H = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public static IBlockState getStateFromMeta(IBlockState state, int meta)
    {
        return state.withProperty(FACING_H, EnumFacing.getHorizontal(meta));
    }

    public static int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING_H).getHorizontalIndex();
    }

    public static IBlockState getStateForPlacement(IBlockState state, EntityLivingBase placer)
    {
        return state.withProperty(FACING_H, placer.getHorizontalFacing().getOpposite());
    }

    public static IBlockState withMirror(IBlockState state, Mirror mirror)
    {
        return state.withProperty(HorizontalFacingController.FACING_H, mirror.mirror(state.getValue(HorizontalFacingController.FACING_H)));
    }

    public static IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(HorizontalFacingController.FACING_H, rot.rotate(state.getValue(HorizontalFacingController.FACING_H)));
    }
}
