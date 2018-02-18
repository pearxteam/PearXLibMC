package ru.pearx.libmc.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockSlave;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockCapabilityEvent;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 13.11.17 18:24.
 */
public class TileMultiblockSlave extends TileSyncable implements IMultiblockSlave
{
    private BlockPos absMasterPos;

    @Override
    public BlockPos getMasterPos()
    {
        return absMasterPos;
    }

    @Override
    public void setMasterPos(BlockPos pos)
    {
        absMasterPos = pos;
    }

    @Override
    public void readCustomData(NBTTagCompound tag)
    {
        if(tag.hasKey("masterPos", Constants.NBT.TAG_INT_ARRAY))
        {
            int[] ints = tag.getIntArray("masterPos");
            setMasterPos(new BlockPos(getPos().getX() + ints[0], getPos().getY() + ints[1], getPos().getZ() + ints[2]));
        }
    }

    @Override
    public void writeCustomData(NBTTagCompound tag)
    {
        BlockPos rel = getMasterPos().subtract(getPos());
        tag.setIntArray("masterPos", new int[] {rel.getX(), rel.getY(), rel.getZ()});
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        T t = Multiblock.sendEventToMaster(getWorld(), getPos(), new MultiblockCapabilityEvent.Get<>(capability, facing), null);
        return t != null ? t : super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return Multiblock.sendEventToMaster(getWorld(), getPos(), new MultiblockCapabilityEvent.Has<>(capability, facing), false) || super.hasCapability(capability, facing);
    }
}
