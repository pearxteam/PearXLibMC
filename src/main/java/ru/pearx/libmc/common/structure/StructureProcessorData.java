package ru.pearx.libmc.common.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/*
 * Created by mrAppleXZ on 24.09.17 19:50.
 */
public abstract class StructureProcessorData
{
    private BlockPos absolutePos;

    public BlockPos getAbsolutePos()
    {
        return absolutePos;
    }

    public void setAbsolutePos(BlockPos absolutePos)
    {
        this.absolutePos = absolutePos;
    }

    public abstract NBTTagCompound serialize();
    public abstract void deserialize(NBTTagCompound tag);
}
