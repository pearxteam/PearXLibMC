package ru.pearx.libmc.client.gui;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by mrAppleXZ on 26.05.17 13:51.
 */

/**
 * A GUI Screen interface.
 */
@SideOnly(Side.CLIENT)
public interface IGuiScreen
{
    /**
     * The width of the screen.
     */
    int getWidth();

    /**
     * The height of the screen.
     */
    int getHeight();


    /**
     * The X position of the mouse pointer.
     */
    int getMouseX();

    /**
     * The Y position of the mouse pointer.
     */
    int getMouseY();

    /**
     * Gets the item renderer.
     */
    RenderItem getRenderItem();

    /**
     * Renders an item's tooltip on the screen.
     * @param stack ItemStack whose tooltip you want to draw.
     * @param x X position on the screen.
     * @param y Y position on the screen.
     */
    void drawTooltip(ItemStack stack, int x, int y);
    void drawHovering(String text, int x, int y);
}
