package ru.pearx.libmc.common.structure.multiblock.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;

/*
 * Created by mrAppleXZ on 13.11.17 19:09.
 */
public class MultiblockBreakEvent implements IMultiblockEvent
{
    public static final String ID = "break";
    private World world;
    private BlockPos pos;
    private IBlockState state;

    public MultiblockBreakEvent(World world, BlockPos pos, IBlockState state)
    {
        this.world = world;
        this.pos = pos;
        this.state = state;
    }

    public World getWorld()
    {
        return world;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public IBlockState getState()
    {
        return state;
    }

    @Override
    public String getId()
    {
        return ID;
    }
}
