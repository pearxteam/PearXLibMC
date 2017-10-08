package ru.pearx.libmc.client.gui.drawables;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.IGuiScreen;

/**
 * Created by mrAppleXZ on 15.04.17 9:49.
 */
@SideOnly(Side.CLIENT)
public class ItemDrawable implements IGuiDrawable
{
    public ItemStack stack;
    public float scale;

    public ItemDrawable(ItemStack stack, float scale)
    {
        this.stack = stack;
        this.scale = scale;
    }

    public ItemDrawable(ItemStack stack)
    {
        this(stack, 1);
    }

    @Override
    public void draw(IGuiScreen screen, int x, int y)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        RenderHelper.enableGUIStandardItemLighting();
        screen.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public void drawTooltip(IGuiScreen screen, int x, int y, int mouseX, int mouseY, int screenX, int screenY)
    {
        if(mouseX >= x && mouseX <= x + getWidth() && mouseY >= y && mouseY <= y + getHeight())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-screenX, -screenY, 0);
            screen.drawTooltip(stack, mouseX + screenX, mouseY + screenY);
            GlStateManager.popMatrix();
        }
    }

    public void drawWithTooltip(IGuiScreen screen, int x, int y, int mouseX, int mouseY, int screenX, int screenY)
    {
        draw(screen, x, y);
        drawTooltip(screen, x, y, mouseX, mouseY, screenX, screenY);
    }

    @Override
    public int getWidth()
    {
        return (int)(16 * scale);
    }

    @Override
    public int getHeight()
    {
        return (int)(16 * scale);
    }
}
