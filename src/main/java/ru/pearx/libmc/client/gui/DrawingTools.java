package ru.pearx.libmc.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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
        for(String s : str.split(System.lineSeparator()))
        {
            rend.drawString(s, 0, ytr, col.getARGB(), shadow);
            ytr += getFontHeight();
        }
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
        for(String s : rend.wrapFormattedStringToWidth(str, width).split("\n|" + System.lineSeparator()))
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
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(str);
    }

    public static int measureChar(char ch)
    {
        return Minecraft.getMinecraft().fontRenderer.getCharWidth(ch);
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

    public static void drawGradientRect(int x, int y, int width, int height, Color c1, Color c2)
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
        bld.pos(x, y, 0).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        bld.pos(x, bottom, 0).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        bld.pos(right, bottom, 0).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
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

    public static int drawStencil(int w, int h)
    {
        //todo REWRITE STENCILING
        int bit = MinecraftForgeClient.reserveStencilBit();
        int flag = 1 << bit;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilFunc(GL11.GL_ALWAYS, flag, flag);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GL11.glStencilMask(flag);
        GL11.glColorMask(false, false, false, false);
        GL11.glDepthMask(false);
        GL11.glClearStencil(0);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        DrawingTools.drawRectangle(0, 0, w, h);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glStencilFunc(GL11.GL_EQUAL, flag, flag);
        GL11.glStencilMask(0);
        GL11.glColorMask(true, true, true, true);
        GL11.glDepthMask(true);
        return bit;
    }

    public static void removeStencil(int bit)
    {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        MinecraftForgeClient.releaseStencilBit(bit);
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
        rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
