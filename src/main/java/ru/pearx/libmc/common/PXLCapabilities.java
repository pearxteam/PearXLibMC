package ru.pearx.libmc.common;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.animation.AnimationStateManager;
import ru.pearx.libmc.common.animation.IAnimationStateManager;

import javax.annotation.Nullable;

/*
 * Created by mrAppleXZ on 21.09.17 10:38.
 */
public class PXLCapabilities
{
    @CapabilityInject(IAnimationStateManager.class)
    public static final Capability<IAnimationStateManager> ASM = null;

    public static final ResourceLocation ASM_NAME = new ResourceLocation(PXLMC.MODID, "animation_state_manager");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IAnimationStateManager.class, new Capability.IStorage<IAnimationStateManager>()
        {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IAnimationStateManager> capability, IAnimationStateManager instance, EnumFacing side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<IAnimationStateManager> capability, IAnimationStateManager instance, EnumFacing side, NBTBase nbt)
            {
            }
        }, AnimationStateManager.class);
    }
}
