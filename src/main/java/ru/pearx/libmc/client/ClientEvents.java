package ru.pearx.libmc.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.models.ModelMultiblock;

/*
 * Created by mrAppleXZ on 24.12.17 10:42.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = PXLMC.MODID, value = Side.CLIENT)
public class ClientEvents
{
    @SubscribeEvent
    public static void onBake(ModelBakeEvent e)
    {
        ModelMultiblock mb = new ModelMultiblock();
        mb.bake();
        e.getModelRegistry().putObject(new ModelResourceLocation(new ResourceLocation(PXLMC.MODID, "multiblock"), "normal"), mb);
    }
}
