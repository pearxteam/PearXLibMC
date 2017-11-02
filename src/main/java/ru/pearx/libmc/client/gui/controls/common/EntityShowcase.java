package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.DrawingTools;

/**
 * Created by mrAppleXZ on 02.07.17 12:53.
 */
@SideOnly(Side.CLIENT)
public class EntityShowcase extends AbstractShowcase
{
    public EntityLivingBase entity;
    public Class<? extends EntityLivingBase> clazz;

    public EntityShowcase(Class<? extends EntityLivingBase> clazz)
    {
        this.clazz = clazz;
        scale = 75;
        rotX = 45;
        rotY = 45;
    }

    @Override
    public void render()
    {
        if(entity == null || entity.getClass() != clazz)
            try
            {
                entity = clazz.getDeclaredConstructor(World.class).newInstance(Minecraft.getMinecraft().world);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        //draw the entity
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, scale / 5, 150);
        DrawingTools.drawEntity(entity, getWidth() / 2, getHeight() / 2, scale, rotX, rotY, 0);
        GlStateManager.popMatrix();
    }
}
