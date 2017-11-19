package ru.pearx.libmc.common.structure.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/*
 * Created by mrAppleXZ on 19.11.17 16:25.
 */
public interface IMultiblockPart
{
    BlockPos getPos();
    World getWorld();
}
