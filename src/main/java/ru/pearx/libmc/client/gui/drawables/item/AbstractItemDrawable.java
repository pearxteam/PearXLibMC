package ru.pearx.libmc.client.gui.drawables.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.IGuiScreen;
import ru.pearx.libmc.client.gui.drawables.IGuiDrawable;

/*
 * Created by mrAppleXZ on 27.10.17 22:10.
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractItemDrawable implements IGuiDrawable
{
    private float scale;

    @Override
    public void draw(IGuiScreen screen, int x, int y)
    {
        DrawingTools.drawItemStackGUI(getRenderStack(), screen.getRenderItem(), Minecraft.getMinecraft().fontRenderer, x, y, scale);
    }

    public void drawTooltip(IGuiScreen screen, int x, int y, int mouseX, int mouseY, int screenX, int screenY)
    {
        if(mouseX >= x && mouseX <= x + getWidth() && mouseY >= y && mouseY <= y + getHeight())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-screenX, -screenY, 0);
            screen.drawTooltip(getRenderStack(), mouseX + screenX, mouseY + screenY);
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

    public abstract ItemStack getRenderStack();

    public float getScale()
    {
        return scale;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }
}
