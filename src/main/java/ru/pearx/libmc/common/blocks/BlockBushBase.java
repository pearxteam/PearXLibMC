package ru.pearx.libmc.common.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

/*
 * Created by mrAppleXZ on 31.08.17 17:47.
 */
public class BlockBushBase extends BlockBush
{
    public BlockBushBase()
    {
        setSoundType(SoundType.PLANT);
    }

    public BlockBushBase(Material materialIn)
    {
        super(materialIn);
        setSoundType(SoundType.PLANT);
    }

    public BlockBushBase(Material materialIn, MapColor mapColorIn)
    {
        super(materialIn, mapColorIn);
        setSoundType(SoundType.PLANT);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return EnumPlantType.Plains;
    }
}
