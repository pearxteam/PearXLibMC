package ru.pearx.libmc.common.animation;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

/*
 * Created by mrAppleXZ on 21.09.17 10:51.
 */
public interface IAnimationStateManager extends INBTSerializable<NBTTagCompound>
{
    void changeState(String state);
    String getState();
    List<String> getAvailableStates();
}
