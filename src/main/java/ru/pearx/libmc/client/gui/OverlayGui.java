package ru.pearx.libmc.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

/*
 * Created by mrAppleXZ on 15.08.17 11:12.
 */
@SideOnly(Side.CLIENT)
public class OverlayGui implements IGuiScreen
{
    public static final OverlayGui INSTANCE = new OverlayGui();

    @Override
    public int getWidth()
    {
        return Minecraft.getMinecraft().displayWidth;
    }

    @Override
    public int getHeight()
    {
        return Minecraft.getMinecraft().displayHeight;
    }

    @Override
    public int getMouseX()
    {
        return Mouse.getEventX();
    }

    @Override
    public int getMouseY()
    {
        return getHeight() - Mouse.getEventY() - 1;
    }

    @Override
    public RenderItem getRenderItem()
    {
        return Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void drawTooltip(ItemStack stack, int x, int y)
    {
        //not implemented.
    }

    @Override
    public void drawHovering(String text, int x, int y)
    {
        //not implemented.
    }

    @Override
    public void close()
    {

    }
}
