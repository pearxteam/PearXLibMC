package ru.pearx.libmc.common.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.pearx.libmc.PXLMC;

/*
 * Created by mrAppleXZ on 20.08.17 23:20.
 */
@Mod.EventBusSubscriber(modid = PXLMC.MODID)
@GameRegistry.ObjectHolder(PXLMC.MODID)
public class PXLBlocks
{
    public static final Block structure_nothing = null;

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> e)
    {
        e.getRegistry().register(new BlockStructureNothing());
    }
}
