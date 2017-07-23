package ru.pearx.libmc.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

/*
 * Created by mrAppleXZ on 22.07.17 13:21.
 */
public abstract class PXLFastTESR<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        renderPre(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, partialTicks, buffer);

        tessellator.draw();
        renderPost(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

    public abstract void renderPre(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);
    public abstract void renderPost(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);
}
