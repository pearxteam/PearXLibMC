package ru.pearx.libmc.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * Created by mrAppleXZ on 27.08.17 12:15.
 */
@Mod.EventBusSubscriber(modid = PXLMC.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ParticleEngine
{
    private static WorldClient lastWorld;
    private static List<PXParticle> particles = new ArrayList<>();

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent e)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        for(PXParticle part : particles)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * e.getPartialTicks()), -(player.lastTickPosY + (player.posY - player.lastTickPosY) * e.getPartialTicks()), -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * e.getPartialTicks()));
            GlStateManager.translate(part.getX(), part.getY(), part.getZ());
            GlStateManager.scale(part.getScaleFactor(), part.getScaleFactor(), part.getScaleFactor());
            GlStateManager.scale(-1, -1, -1);
            double width = part.getWidth();
            double height = part.getHeight();
            GlStateManager.translate(-(width / 2), -(height / 2), 0);

            GlStateManager.translate(width / 2, height / 2, 0);
            GlStateManager.rotate(-player.getRotationYawHead(), 0, 1, 0);
            GlStateManager.rotate(player.rotationPitch, 1, 0, 0);
            GlStateManager.translate(-(width / 2), -(height / 2), 0);

            part.onRender();
            GlStateManager.popMatrix();
        }
    }

    private static List<PXParticle> toRemove = new ArrayList<>();

    @SubscribeEvent
    public static void onWorldTick(TickEvent.ClientTickEvent e)
    {
        if(e.side == Side.CLIENT)
        {
            if(lastWorld != null && lastWorld != Minecraft.getMinecraft().world)
                particles.clear();
            lastWorld = Minecraft.getMinecraft().world;
            for(PXParticle part : particles)
            {
                part.onUpdate();
                if(part.isExpired())
                    toRemove.add(part);
            }
            particles.removeAll(toRemove);
            toRemove.clear();
        }
    }

    public static void addParticle(PXParticle part)
    {
        particles.add(part);
    }

}
