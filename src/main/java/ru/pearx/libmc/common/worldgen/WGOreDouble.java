package ru.pearx.libmc.common.worldgen;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by mrAppleXZ on 05.06.17 9:28.
 */
public class WGOreDouble extends WGOre
{
    private IBlockState up;

    public WGOreDouble(int minVeinSize, int maxVeinSize, int minY, int maxY, float chance, IBlockState up, IBlockState down, List<Integer> dims, boolean whitelist)
    {
        super(minVeinSize, maxVeinSize, minY, maxY, chance, down, dims, whitelist, 0, 0, new Predicate<IBlockState>()
        {
            @Override
            public boolean apply(@Nullable IBlockState input)
            {
                return true;
            }
        });
        this.up = up;
    }
}
