package ru.pearx.libmc.common.structure;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import ru.pearx.libmc.PXLMC;

/*
 * Created by mrAppleXZ on 24.09.17 19:59.
 */
@Mod.EventBusSubscriber(modid = PXLMC.MODID)
public class StructureProcessorRegistry
{
    public static IForgeRegistry<IStructureProcessor> REGISTRY = new RegistryBuilder<IStructureProcessor>().setName(new ResourceLocation(PXLMC.MODID, "structure_processor")).setType(IStructureProcessor.class).create();
}
