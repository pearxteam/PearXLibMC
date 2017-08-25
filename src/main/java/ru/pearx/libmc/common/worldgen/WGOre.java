package ru.pearx.libmc.common.worldgen;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by mrAppleXZ on 19.05.17 10:16.
 */
public class WGOre implements IWorldGenerator
{
    private int minVeinSize, maxVeinSize, minY, maxY, minVeins, maxVeins;
    private float chance;
    private List<Integer> dims;
    private boolean whitelist;
    private WorldGenMinable wgm;

    public WGOre(int minVeinSize, int maxVeinSize, int minY, int maxY, float chance, IBlockState state, List<Integer> dims, boolean whitelist, int minVeins, int maxVeins, Predicate<IBlockState> predicate)
    {
        this.minVeinSize = minVeinSize;
        this.maxVeinSize = maxVeinSize;
        this.minY = minY;
        this.maxY = maxY;
        this.chance = chance;
        this.dims = dims;
        this.whitelist = whitelist;
        this.minVeins = minVeins;
        this.maxVeins = maxVeins;
        wgm = new WorldGenMinable(state, 0, predicate);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        //if in a right dimension
        if (dims.contains(world.provider.getDimension()) == whitelist)
        {
            //for each vein
            int veins = random.nextInt(maxVeins - minVeins + 1) + minVeins;
            for (int i = 0; i < veins; i++)
            {
                //if we're lucky
                if (random.nextFloat() <= chance)
                {
                    wgm.numberOfBlocks = random.nextInt(maxVeinSize - minVeinSize + 1) + minVeinSize;
                    wgm.generate(world, random, new BlockPos(chunkX * 16 + random.nextInt(16), random.nextInt(maxY - minY + 1) + minY, chunkZ * 16 + random.nextInt(16)));
                }
            }
        }
    }

    public static class StonePredicate implements Predicate<IBlockState>
    {
        @Override
        public boolean apply(@Nullable IBlockState state)
        {
            return state != null && state.getBlock() == Blocks.STONE && state.getValue(BlockStone.VARIANT).isNatural();
        }
    }
}
