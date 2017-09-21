package ru.pearx.libmc.common.animation;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.networking.packets.CPacketSyncASMState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/*
 * Created by mrAppleXZ on 21.09.17 11:13.
 */
public class AnimationStateManager implements IAnimationStateManager
{
    private TileEntity tile;
    private String state;
    private List<String> states;

    public AnimationStateManager(TileEntity te, String defaultState, String... states)
    {
        this.tile = te;
        this.state = defaultState;
        this.states = Arrays.asList(states);
    }

    @Override
    public void changeState(String state)
    {
        if(!states.contains(state))
            throw new IllegalArgumentException("state");
        //client
        if(tile.getWorld().isRemote)
        {
            this.state = state;
        }
        //server
        else
        {
            this.state = state;
            WorldServer world = (WorldServer) tile.getWorld();
            PlayerChunkMapEntry entr = world.getPlayerChunkMap().getEntry(tile.getPos().getX() / 16, tile.getPos().getZ() / 16);
            for(EntityPlayerMP p : entr.players)
            {
                PXLMC.NETWORK.sendTo(new CPacketSyncASMState(tile.getPos(), getAvailableStates().indexOf(state)), p);
            }
        }
    }

    @Override
    public String getState()
    {
        return state;
    }

    @Override
    public List<String> getAvailableStates()
    {
        return states;
    }
}
