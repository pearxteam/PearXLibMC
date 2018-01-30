package ru.pearx.libmc.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import ru.pearx.lib.Color;

/**
 * Created by mrAppleXZ on 16.04.17 20:45.
 */
@SideOnly(Side.CLIENT)
public class DrawingTools
{
    public static void drawTexture(ResourceLocation tex, int x, int y, int width, int height, int u, int v, int texw, int texh)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, texw, texh);
    }

    public static void drawTexture(ResourceLocation tex, int x, int y, int width, int height)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
    }

    public static void drawString(String str, int x, int y, Color col, boolean shadow, float scale, FontRenderer rend)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 0);
        int ytr = 0;
        for(String s : str.split("\r\n|\n|\r"))
        {
            rend.drawString(s, 0, ytr, col.getARGB(), shadow);
            ytr += getFontHeight();
        }
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static void drawString(String str, int x, int y, Color col, boolean shadow, float scale)
    {
        drawString(str, x, y, col, shadow, scale, Minecraft.getMinecraft().fontRenderer);
    }

    public static void drawString(String str, int x, int y, Color col, boolean shadow)
    {
        drawString(str, x, y, col, shadow, 1, Minecraft.getMinecraft().fontRenderer);
    }

    public static void drawString(String str, int x, int y, Color col)
    {
        drawString(str, x, y, col, true);
    }

    public static void drawString(String str, int x, int y, Color col, int width, boolean shadow, FontRenderer rend)
    {
        GlStateManager.pushMatrix();
        rend.resetStyles();
        str = rend.trimStringNewline(str);
        for(String s : rend.wrapFormattedStringToWidth(str, width).split("\r\n|\r|\n"))
        {
            if(shadow)
                rend.renderStringAligned(s, x + 1, y + 1, width, col.getARGB(), true);
            rend.renderStringAligned(s, x, y, width, col.getARGB(), false);
            y += getFontHeight();
        }
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static void drawString(String str, int x, int y, Color col, int width, boolean shadow)
    {
        drawString(str, x, y, col, width, shadow, Minecraft.getMinecraft().fontRenderer);
    }

    public static void drawString(String str, int x, int y, Color col, int width)
    {
        drawString(str, x, y, col, width, true);
    }

    public static int measureString(String str)
    {
        int w = 0;
        for(String s : str.split("(?:\r\n|\r|\n)"))
        {
            int i = Minecraft.getMinecraft().fontRenderer.getStringWidth(s);
            if(i > w)
                w = i;
        }
        return w;
    }

    public static int measureChar(char ch)
    {
        return Minecraft.getMinecraft().fontRenderer.getCharWidth(ch);
    }

    public static int getStringHeight(String str)
    {
        str = Minecraft.getMinecraft().fontRenderer.trimStringNewline(str);
        int y = 0;
        for(String s : str.split("(?:\r\n|\r|\n)"))
            y += getFontHeight();
        return y;
    }

    public static int getStringHeight(String str, int width)
    {
        str = Minecraft.getMinecraft().fontRenderer.trimStringNewline(str);
        int y = 0;
        for(String s : Minecraft.getMinecraft().fontRenderer.wrapFormattedStringToWidth(str, width).split("\r\n|\r|\n"))
            y += getFontHeight();
        return y;
    }

    public static int getFontHeight()
    {
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }

    public static void drawHoveringText(String str, int x, int y, Color c, boolean shadow, float scale, FontRenderer rend)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(10, 0, 1);
        drawString(str, x, y, c, shadow, scale, rend);
        GlStateManager.popMatrix();
    }

    public static void drawGradientRect(int x, int y, int width, int height, Color c1, Color c2, Color c3, Color c4)
    {
        int right = x + width;
        int bottom = y + height;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bld = tessellator.getBuffer();
        bld.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bld.pos(right, y, 0).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        bld.pos(x, y, 0).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        bld.pos(x, bottom, 0).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        bld.pos(right, bottom, 0).color(c4.getRed(), c4.getGreen(), c4.getBlue(), c4.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientRect(int x, int y, int width, int height, Color c)
    {
        drawGradientRect(x, y, width, height, c, c, c, c);
    }

    public static void drawLine(int x1, int y1, int x2, int y2, int width, Color c1, Color c2)
    {
        GlStateManager.glLineWidth(width);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bld = tessellator.getBuffer();
        bld.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        bld.pos(x1, y1, 0).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        bld.pos(x2, y2, 0).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.glLineWidth(1);
    }

    public static void drawRectangle(int x, int y, int width, int height)
    {
        int bottom = y + height;
        int right = x + width;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bld = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        bld.begin(7, DefaultVertexFormats.POSITION);
        bld.pos((double)x, (double)bottom, 0.0D).endVertex();
        bld.pos((double)right, (double)bottom, 0.0D).endVertex();
        bld.pos((double)right, (double)y, 0.0D).endVertex();
        bld.pos((double)x, (double)y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
    }

    public static void drawEntity(Entity ent, float x, float y, float scale, float rotX, float rotY, float rotZ)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(rotX, 1, 0, 0);
        GlStateManager.rotate(rotY, 0, 1, 0);
        GlStateManager.rotate(rotZ, 0, 0, 1);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawItemStackGUI(ItemStack stack, RenderItem rend, FontRenderer frend, int x, int y, float scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, scale);
        RenderHelper.enableGUIStandardItemLighting();
        rend.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().player, stack, 0, 0);
        rend.renderItemOverlays(frend, stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
