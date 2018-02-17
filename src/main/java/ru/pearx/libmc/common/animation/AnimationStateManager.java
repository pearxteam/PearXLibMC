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
    private List<AnimationElement> elements;

    public AnimationStateManager(TileEntity te, AnimationElement... elements)
    {
        this.tile = te;
        this.elements = Arrays.asList(elements);
    }

    @Override
    public List<AnimationElement> getElements()
    {
        return elements;
    }

    @Override
    public AnimationElement getElement(String name)
    {
        for (AnimationElement el : getElements())
            if (el.getName().equals(name))
                return el;
        return null;
    }

    @Override
    public void changeState(int element, int index)
    {
        getElements().get(element).setStateIndex(index);
        if (!tile.getWorld().isRemote)
        {
            PXLMC.NETWORK.sendToAllAround(new CPacketSyncASMState(tile.getPos(), element, index), new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 256));
        }
    }

    @Override
    public void changeState(String name, String newState)
    {
        for(int i = 0; i < getElements().size(); i++)
        {
            AnimationElement el = getElements().get(i);
            if(el.getName().equals(name))
            {
                int stateInd = el.getStates().indexOf(newState);
                if (stateInd < 0)
                    throw new IllegalArgumentException("\"" + newState + "\" is not a valid state for an element \"" + name + "\"! Valid states are " + el.getStates() + ".");
                changeState(i, stateInd);
                return;
            }
        }
        throw new IllegalArgumentException("\"" + name + "\" is not a valid element.");
    }
}
