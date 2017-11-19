package ru.pearx.libmc.common.structure.multiblock.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;

/*
 * Created by mrAppleXZ on 17.11.17 7:33.
 */
public class MultiblockPickBlockEvent implements IMultiblockEvent<ItemStack>
{
    public static final String ID = "get_pick_block";

    private IBlockState state;
    private RayTraceResult target;
    private EntityPlayer player;

    public MultiblockPickBlockEvent(IBlockState state, RayTraceResult target, EntityPlayer player)
    {
        this.state = state;
        this.target = target;
        this.player = player;
    }

    public IBlockState getState()
    {
        return state;
    }

    public RayTraceResult getTarget()
    {
        return target;
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    @Override
    public String getId()
    {
        return ID;
    }
}
