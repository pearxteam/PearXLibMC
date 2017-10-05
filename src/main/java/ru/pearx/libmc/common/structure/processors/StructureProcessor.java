package ru.pearx.libmc.common.structure.processors;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import ru.pearx.libmc.PXLMC;

/*
 * Created by mrAppleXZ on 26.09.17 14:11.
 */
public abstract class StructureProcessor extends IForgeRegistryEntry.Impl<IStructureProcessor> implements IStructureProcessor
{
    public static final IForgeRegistry<IStructureProcessor> REGISTRY = new RegistryBuilder<IStructureProcessor>().setName(new ResourceLocation(PXLMC.MODID, "structure_processor")).setType(IStructureProcessor.class).create();
}
