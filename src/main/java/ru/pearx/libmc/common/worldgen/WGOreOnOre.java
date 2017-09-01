package ru.pearx.libmc.common.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/*
 * Created by mrAppleXZ on 20.08.17 11:09.
 */
public class WGOreOnOre implements IWorldGenerator
{
    public interface OrePredicate
    {
        boolean canSpawn(World world, BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos posUp);
    }

    public int minY, maxY;
    public float chance;
    public IBlockState state;
    public OrePredicate predicate;
    public List<Integer> dimensionList;
    public boolean whitelist;

    public WGOreOnOre(int minY, int maxY, float chance, IBlockState state, OrePredicate predicate, List<Integer> dimensionList, boolean whitelist)
    {
        this.minY = minY;
        this.maxY = maxY;
        this.chance = chance;
        this.state = state;
        this.predicate = predicate;
        this.dimensionList = dimensionList;
        this.whitelist = whitelist;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (dimensionList.contains(world.provider.getDimension()) == whitelist)
        {
            BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos upper = new BlockPos.MutableBlockPos();
            for (int x = 0; x < 16; x++)
            {
                for (int y = minY; y <= maxY; y++)
                {
                    for (int z = 0; z < 16; z++)
                    {
                        mut.setPos(chunkX * 16 + x, y, chunkZ * 16 + z);
                        upper.setPos(chunkX * 16 + x, y + 1, chunkZ * 16 + z);
                        if (predicate.canSpawn(world, mut, upper))
                        {
                            if (random.nextFloat() <= chance)
                            {
                                world.setBlockState(upper, state, 2);
                            }
                        }
                    }
                }
            }
        }
    }
}
