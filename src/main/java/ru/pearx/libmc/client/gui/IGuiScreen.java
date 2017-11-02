package ru.pearx.libmc.client.gui;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by mrAppleXZ on 26.05.17 13:51.
 */
@SideOnly(Side.CLIENT)
public interface IGuiScreen
{
    int getWidth();
    int getHeight();

    int getMouseX();
    int getMouseY();

    RenderItem getRenderItem();

    void drawTooltip(ItemStack stack, int x, int y);
    void drawHovering(String text, int x, int y);
}
