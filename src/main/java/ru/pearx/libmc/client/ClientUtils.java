package ru.pearx.libmc.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

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
