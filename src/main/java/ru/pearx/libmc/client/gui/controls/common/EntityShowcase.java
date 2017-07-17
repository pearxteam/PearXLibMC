package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.Control;

/**
 * Created by mrAppleXZ on 02.07.17 12:53.
 */
@SideOnly(Side.CLIENT)
public class EntityShowcase extends Control
{
    public int scale = 75;
    public EntityLivingBase entity;
    public Class<? extends EntityLivingBase> clazz;
    public int prevMouseX, prevMouseY;
    public int rotX, rotY;
    public boolean act1, act2;

    public EntityShowcase(Class<? extends EntityLivingBase> clazz)
    {
        this.clazz = clazz;
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
        //draw two 16x16 icons
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.translate(0, 0, 900);
        GlStateManager.scale(0.5f, 0.5f, 0);
        DrawingTools.drawTexture(new ResourceLocation(PXLMC.MODID, "textures/gui/rotatable.png"), 0, 0, 24, 24);
        DrawingTools.drawTexture(new ResourceLocation(PXLMC.MODID, "textures/gui/scalable.png"), 24, 0, 24, 24);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        //draw the entity
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, scale / 5, 150);
        DrawingTools.drawEntity(entity, getWidth() / 2, getHeight() / 2, scale, rotX, rotY, 0);
        GlStateManager.popMatrix();

        if(act1 && new Rectangle(0, 0, 12, 12).contains(prevMouseX, prevMouseY))
            getGuiScreen().drawHovering(I18n.format("hovering.rotatable.text"), prevMouseX, prevMouseY);
        if(act2 && new Rectangle(12, 0, 12, 12).contains(prevMouseX, prevMouseY))
            getGuiScreen().drawHovering(I18n.format("hovering.scalable.text"), prevMouseX, prevMouseY);
    }

    @Override
    public void mouseWheel(int delta)
    {
        if(isFocused())
        {
            scale += delta / 120;
            if(scale < 0)
                scale = 0;
        }
    }

    @Override
    public void mouseMove(int x, int y, int dx, int dy)
    {
        if(Mouse.isButtonDown(0))
        {
            int mX = x - prevMouseX;
            int mY = y - prevMouseY;
            rotY += mX;
            rotX += mY;
            if(rotX > 360)
                rotX -= 360;
            if(rotY > 360)
                rotY -= 360;
            if(rotX < 0)
                rotX += 360;
            if(rotY < 0)
                rotY += 360;
        }
        prevMouseX = x;
        prevMouseY = y;
    }

    @Override
    public void mouseLeave()
    {
        act1 = false;
        act2 = false;
    }

    @Override
    public void mouseEnter()
    {
        act1 = true;
        act2 = true;
    }
}