package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/*
 * Created by mrAppleXZ on 12.11.17 16:00.
 */

/**
 * A multiblock slave part. Contains only a reference to the master part. All the events should be sent directly to the master part. This interface should be implemented by a {@link TileEntity}.
 */
public interface IMultiblockSlave extends IMultiblockPart
{
    /**
     * Gets the master part position.
     */
    BlockPos getMasterPos();

    /**
     * Sets the master part position.
     * @param pos Position.
     */
    void setMasterPos(BlockPos pos);

    /**
     * Gets the master part.
     * @return The master part. Returns null if an error occurs while getting the master part.
     */
    @Override
    default IMultiblockMaster getMaster()
    {
        TileEntity te = getWorld().getTileEntity(getMasterPos());
        return te instanceof IMultiblockMaster ? (IMultiblockMaster) te : null;
    }
}
