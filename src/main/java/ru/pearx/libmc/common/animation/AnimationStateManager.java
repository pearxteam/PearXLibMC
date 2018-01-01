package ru.pearx.libmc.common.animation;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.networking.packets.CPacketSyncASMState;

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
            PXLMC.NETWORK.sendToAllAround(new CPacketSyncASMState(tile.getPos(), getAvailableStates().indexOf(state)), new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 256));
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
