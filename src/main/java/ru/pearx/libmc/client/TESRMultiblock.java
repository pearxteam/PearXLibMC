package ru.pearx.libmc.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import ru.pearx.libmc.common.structure.multiblock.IMultiblockMaster;

/*
 * Created by mrAppleXZ on 23.02.18 16:54.
 */
public abstract class TESRMultiblock<T extends TileEntity & IMultiblockMaster> extends PXLFastTESR<T>
{
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if(!te.isInactive())
            super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}
