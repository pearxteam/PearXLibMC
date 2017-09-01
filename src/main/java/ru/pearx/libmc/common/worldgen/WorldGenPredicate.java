package ru.pearx.libmc.common.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/*
 * Created by mrAppleXZ on 01.09.17 11:09.
 */
public interface WorldGenPredicate
{
    boolean canGenerateHere(World world, BlockPos pos, Random rand, IBlockState toGenerate);
}
