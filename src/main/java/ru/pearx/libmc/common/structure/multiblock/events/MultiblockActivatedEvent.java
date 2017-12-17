package ru.pearx.libmc.common.structure.multiblock.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockEvent;

/*
 * Created by mrAppleXZ on 13.11.17 19:13.
 */
public class MultiblockActivatedEvent implements IMultiblockEvent<Boolean>
{
    public static final String ID = "activated";
    private IBlockState state;
    private EntityPlayer player;
    private EnumHand hand;
    private EnumFacing facing;
    private float hitX, hitY, hitZ;

    public MultiblockActivatedEvent(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        this.state = state;
        this.player = player;
        this.hand = hand;
        this.facing = facing;
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitZ = hitZ;
    }

    public IBlockState getState()
    {
        return state;
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public EnumHand getHand()
    {
        return hand;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    public float getHitX()
    {
        return hitX;
    }

    public float getHitY()
    {
        return hitY;
    }

    public float getHitZ()
    {
        return hitZ;
    }

    @Override
    public String getId()
    {
        return ID;
    }
}
