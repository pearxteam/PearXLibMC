package ru.pearx.libmc.common.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;

/*
 * Created by mrAppleXZ on 01.09.17 10:48.
 */
public class WGGround implements IWorldGenerator
{

    public int minY, maxY, minCount, maxCount;
    public float chance;
    public List<Integer> dimList;
    public boolean dimListMode;
    public IBlockState state;
    public WorldGenPredicate predicate;

    public WGGround(IBlockState state, WorldGenPredicate predicate, int minY, int maxY, int minCount, int maxCount, float chance, List<Integer> dimList, boolean dimListMode)
    {
        this.minY = minY;
        this.maxY = maxY;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.chance = chance;
        this.dimList = dimList;
        this.dimListMode = dimListMode;
        this.state = state;
        this.predicate = predicate;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        //if in a right dimension
        if (dimList.contains(world.provider.getDimension()) == dimListMode)
        {
            //for each block
            int count = random.nextInt(maxCount - minCount + 1) + minCount;
            for (int i = 0; i < count; i++)
            {
                //if we're lucky
                if (random.nextFloat() <= chance)
                {
                    int x = chunkX * 16 + 8 + random.nextInt(16);
                    int z = chunkZ * 16 + 8 + random.nextInt(16);
                    BlockPos pos = new BlockPos(x, world.getHeight(x, z), z);

                    //if in the height range
                    if (pos.getY() >= minY && pos.getY() <= maxY)
                    {
                        if(predicate.canGenerateHere(world, pos, random, state))
                        {
                            world.setBlockState(pos, state, 2);
                        }
                    }
                }
            }
        }
    }
}
