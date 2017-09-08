package ru.pearx.libmc.client.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.lib.Color;
import ru.pearx.libmc.client.gui.DrawingTools;

/**
 * Created by mrAppleXZ on 28.05.17 20:19.
 */
@SideOnly(Side.CLIENT)
public class ParticleTrail extends PXParticle
{
    private Color color;
    private ResourceLocation texture;
    private int width, height;
    private float scale;
    private float alpha, baseAlpha;

    public ParticleTrail(double x, double y, double z, Color col, float alpha, ResourceLocation texture, int width, int height, float scale, int age)
    {
        super(x, y, z);
        this.color = col;
        this.texture = texture;
        this.scale = scale;
        this.baseAlpha = alpha;
        this.width = width;
        this.height = height;
        setMaxAge(age);
    }

    @Override
    public double getWidth()
    {
        return width;
    }

    @Override
    public double getHeight()
    {
        return height;
    }

    @Override
    public float getScaleFactor()
    {
        return super.getScaleFactor() * scale;
    }

    @Override
    public void onRender()
    {
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);
        DrawingTools.drawTexture(texture, 0, 0, width, height);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
    }

    @Override
    public void onUpdate()
    {
        alpha = baseAlpha * (1 - ((float)getAge() / getMaxAge()));
        super.onUpdate();
    }
}
