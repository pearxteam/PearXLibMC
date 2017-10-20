package ru.pearx.libmc.client.gui.drawables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.IGuiScreen;

/**
 * Created by mrAppleXZ on 26.05.17 14:18.
 */
@SideOnly(Side.CLIENT)
public class SimpleDrawable implements IGuiDrawable
{
    private ResourceLocation texture;
    private int texWidth, texHeight;
    private int targetWidth, targetHeight;
    private int u, v;

    public SimpleDrawable(ResourceLocation texture, int texWidth, int texHeight)
    {
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    public SimpleDrawable(ResourceLocation texture, int texWidth, int texHeight, int targetWidth, int targetHeight)
    {
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    public SimpleDrawable(ResourceLocation texture, int texWidth, int texHeight, int targetWidth, int targetHeight, int u, int v)
    {
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.u = u;
        this.v = v;
    }

    @Override
    public int getWidth()
    {
        return targetWidth;
    }

    @Override
    public int getHeight()
    {
        return targetHeight;
    }

    @Override
    public void draw(IGuiScreen screen, int x, int y)
    {
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GuiScreen.drawScaledCustomSizeModalRect(x, y, u, v, targetWidth, targetHeight, targetWidth, targetHeight, texWidth, texHeight);
        GlStateManager.disableBlend();
    }
}
