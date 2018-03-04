package ru.pearx.libmc.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import ru.pearx.libmc.common.nbt.serialization.NBTSerializer;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockSlave;
import ru.pearx.libmc.common.structure.multiblock.Multiblock;
import ru.pearx.libmc.common.structure.multiblock.events.MultiblockCapabilityEvent;
import ru.pearx.libmc.common.tiles.syncable.TileSyncable;
import ru.pearx.libmc.common.tiles.syncable.TileSyncableComposite;
import ru.pearx.libmc.common.tiles.syncable.WriteTarget;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 13.11.17 18:24.
 */
public class TileMultiblockSlave extends TileSyncableComposite implements IMultiblockSlave
{
    public static final String NBT_MASTER_POS = "masterPos";

    private BlockPos absMasterPos;

    public TileMultiblockSlave()
    {
        getSerializers().add(new NBTSerializer.ReaderWriter<>(NBT_MASTER_POS, BlockPos.class, this::setMasterPos, this::getMasterPos));
    }

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
