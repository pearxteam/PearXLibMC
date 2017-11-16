package ru.pearx.libmc.common.blocks.controllers;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import ru.pearx.libmc.PXLMC;

/*
 * Created by mrAppleXZ on 16.11.17 7:34.
 */
public class AxisController
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public static IBlockState getStateFromMeta(IBlockState state, int meta)
    {
        return state.withProperty(AXIS, EnumFacing.Axis.values()[meta]);
    }

    public static int getMetaFromState(IBlockState state)
    {
        return state.getValue(AXIS).ordinal();
    }

    public static IBlockState getStateForPlacement(IBlockState state, EnumFacing facing, EntityLivingBase placer)
    {
        return state.withProperty(AXIS, facing.getAxis());
    }

    public static IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(AXIS, PXLMC.rotateAxis(state.getValue(AXIS), rot));
    }
}
