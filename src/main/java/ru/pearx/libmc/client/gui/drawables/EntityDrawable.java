package ru.pearx.libmc.client.gui.drawables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.IGuiScreen;

/**
 * Created by mrAppleXZ on 28.06.17 9:44.
 */
@SideOnly(Side.CLIENT)
public class EntityDrawable implements IGuiDrawable
{
    public EntityLivingBase entity;
    public Class<? extends EntityLivingBase> clazz;
    public float scale;
    public float yOffset;

    public EntityDrawable(Class<? extends EntityLivingBase> clazz, float scale, float yOff)
    {
        this.clazz = clazz;
        this.scale = scale;
        this.yOffset = yOff;
    }

    @Override
    public int getWidth()
    {
        return 0;
    }

    @Override
    public int getHeight()
    {
        return 0;
    }

    @Override
    public void draw(IGuiScreen screen, int x, int y)
    {
        if(entity == null || entity.getClass() != clazz)
        {
            try
            {
                entity = clazz.getDeclaredConstructor(World.class).newInstance(Minecraft.getMinecraft().world);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, yOffset, 0);
        DrawingTools.drawEntity(entity, x, y, scale, 30, -30, 0);
        GlStateManager.popMatrix();
    }
}
