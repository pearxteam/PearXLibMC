package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.util.math.BlockPos;

/*
 * Created by mrAppleXZ on 12.11.17 16:09.
 */
public interface IMultiblockMaster
{
    BlockPos[] getSlavesPositions();
    void handleEvent(IMultiblockEvent evt);
}
