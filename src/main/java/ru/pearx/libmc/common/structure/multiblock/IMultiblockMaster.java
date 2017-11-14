package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/*
 * Created by mrAppleXZ on 12.11.17 16:09.
 */
public interface IMultiblockMaster
{
    Rotation getRotation();
    void setRotation(Rotation rot);
    List<BlockPos> getSlavesPositions();
    void setSlavesPositions(List<BlockPos> lst);
    void handleEvent(IMultiblockEvent evt);
}
