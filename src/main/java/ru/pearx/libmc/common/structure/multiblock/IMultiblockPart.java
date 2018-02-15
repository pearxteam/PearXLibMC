package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 19.11.17 16:25.
 */

/**
 * A multiblock part. Can be slave and master. This interface should be implemented by a Tile Entity.
 */
public interface IMultiblockPart
{
    /**
     * Gets the part's position in the {@link World} {@link #getWorld()}.
     */
    BlockPos getPos();

    /**
     * Gets the part's {@link World}.
     */
    World getWorld();

    /**
     * Gets the master part.
     * @return The master part. Returns null if an error occurs while getting the master part.
     */
    @Nullable
    IMultiblockMaster getMaster();
}
