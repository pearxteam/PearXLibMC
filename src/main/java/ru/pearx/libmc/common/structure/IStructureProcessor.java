package ru.pearx.libmc.common.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import ru.pearx.libmc.client.gui.structure.ControlStructureProcessor;

import javax.annotation.Nullable;
import java.util.Random;

/*
 * Created by mrAppleXZ on 24.09.17 19:43.
 */
public interface IStructureProcessor extends IForgeRegistryEntry<IStructureProcessor>
{
    @SideOnly(Side.CLIENT)
    ControlStructureProcessor getControl(@Nullable StructureProcessorData data);
    @SideOnly(Side.CLIENT)
    String getName();
    void process(StructureProcessorData data, WorldServer world, Random rand);
    StructureProcessorData loadData(NBTTagCompound tag, BlockPos pos);
}
