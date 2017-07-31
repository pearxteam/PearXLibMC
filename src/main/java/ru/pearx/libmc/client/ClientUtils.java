package ru.pearx.libmc.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
}
