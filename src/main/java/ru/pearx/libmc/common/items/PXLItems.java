package ru.pearx.libmc.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.blocks.PXLBlocks;

/*
 * Created by mrAppleXZ on 20.08.17 23:25.
 */
@Mod.EventBusSubscriber(modid = PXLMC.MODID)
@GameRegistry.ObjectHolder(PXLMC.MODID)
public class PXLItems
{
    public static final ItemBlock structure_nothing = null;

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Item> e)
    {
        e.getRegistry().register(new ItemBlockBase(PXLBlocks.structure_nothing));
    }
}
