package ru.pearx.libmc.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by mrAppleXZ on 10.07.17 22:12.
 */
@SideOnly(Side.CLIENT)
public class ClientUtils
{
    public static void setModelLocation(Item itm, int meta, String suffix)
    {
        ModelLoader.setCustomModelResourceLocation(itm, meta, new ModelResourceLocation(itm.getRegistryName() + suffix, "normal"));
    }

    public static void setModelLocation(Item itm)
    {
        setModelLocation(itm, 0, "");
    }

    public static ModelRotation rotationFromFace(EnumFacing face)
    {
        switch (face)
        {
            case DOWN: return ModelRotation.X90_Y0;
            case UP: return ModelRotation.X270_Y0;
            case NORTH: return ModelRotation.X0_Y0;
            case SOUTH: return ModelRotation.X0_Y180;
            case WEST: return ModelRotation.X0_Y270;
            case EAST: return ModelRotation.X0_Y90;
        }
        return null;
    }

    public static ModelRotation rotationFromFaceReversed(EnumFacing face)
    {
        switch (face)
        {
            case DOWN: return ModelRotation.X270_Y0;
            case UP: return ModelRotation.X90_Y0;
            case NORTH: return ModelRotation.X0_Y180;
            case SOUTH: return ModelRotation.X0_Y0;
            case WEST: return ModelRotation.X0_Y90;
            case EAST: return ModelRotation.X0_Y270;
        }
        return null;
    }
}
