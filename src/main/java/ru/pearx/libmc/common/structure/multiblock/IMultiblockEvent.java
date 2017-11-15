package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Created by mrAppleXZ on 13.11.17 17:08.
 */
public interface IMultiblockEvent<T>
{
    String getId();

    default <R> T cast(R r)
    {
        //noinspection unchecked
        return (T)r;
    }
}
