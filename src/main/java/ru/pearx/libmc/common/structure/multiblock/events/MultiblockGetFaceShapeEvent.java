package ru.pearx.libmc.common.structure.multiblock.events;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;

/*
 * Created by mrAppleXZ on 19.11.17 15:42.
 */
public class MultiblockGetFaceShapeEvent implements IMultiblockEvent<BlockFaceShape>
{
    public static final String ID = "GET_FACE_SHAPE";

    private IBlockState state;
    private EnumFacing facing;

    public MultiblockGetFaceShapeEvent(IBlockState state, EnumFacing facing)
    {
        this.state = state;
        this.facing = facing;
    }

    public IBlockState getState()
    {
        return state;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    @Override
    public String getId()
    {
        return ID;
    }
}
