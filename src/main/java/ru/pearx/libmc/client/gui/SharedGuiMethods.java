package ru.pearx.libmc.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.pearx.libmc.client.gui.controls.GuiControlContainer;

import java.io.IOException;

/*
 * Created by mrAppleXZ on 17.09.17 15:56.
 */
@SideOnly(Side.CLIENT)
public final class SharedGuiMethods
{
    public interface BaseTooltipRenderer
    {
        void render(ItemStack stack, int x, int y);
    }
    public interface BaseHoveringRenderer
    {
        void render(String text, int x, int y);
    }

    public static void handleKeyboardInput(GuiControlContainer container) throws IOException
    {
        if(container != null)
        {
            if(Keyboard.getEventKeyState())
                container.invokeKeyDown(Keyboard.getEventKey());
            else
                container.invokeKeyUp(Keyboard.getEventKey());
        }
    }

    public static void keyTyped(GuiControlContainer container, char typedChar, int keyCode) throws IOException
    {
        if(container != null)
            container.invokeKeyPress(typedChar, keyCode);
    }

    public static void handleMouseInput(GuiScreen g, GuiControlContainer container) throws IOException
    {
        if (container != null)
        {
            int x = getMouseX(g);
            int y = getMouseY(g);
            if (Mouse.getEventButton() != -1)
            {
                if (Mouse.getEventButtonState())
                    container.invokeMouseDown(Mouse.getEventButton(), x - container.getX(), y - container.getY());
                else
                    container.invokeMouseUp(Mouse.getEventButton(), x - container.getX(), y - container.getY());
            }
            if (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0)
            {
                container.invokeMouseMove(x - container.getX(), y - container.getY(), Mouse.getEventDX(), -Mouse.getEventDY());
            }
            if (Mouse.getEventDWheel() != 0)
            {
                container.invokeMouseWheel(Mouse.getEventDWheel());
            }
        }
    }

    public static void drawTooltip(ItemStack stack, int x, int y, BaseTooltipRenderer rend)
    {
        GlStateManager.pushMatrix();
        rend.render(stack, x, y);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static void drawHovering(String text, int x, int y, BaseHoveringRenderer rend)
    {
        GlStateManager.pushMatrix();
        rend.render(text, x, y);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static int getMouseX(GuiScreen g)
    {
        return Mouse.getEventX() * g.width / g.mc.displayWidth;
    }

    public static int getMouseY(GuiScreen g)
    {
        return g.height - Mouse.getEventY() * g.height / g.mc.displayHeight - 1;
    }
}
