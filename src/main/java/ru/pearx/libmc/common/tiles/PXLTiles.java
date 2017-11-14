package ru.pearx.libmc.common.tiles;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ru.pearx.libmc.PXLMC;

/*
 * Created by mrAppleXZ on 14.11.17 13:46.
 */
public final class PXLTiles
{
    public static void setup()
    {
        GameRegistry.registerTileEntity(TileMultiblockSlave.class, new ResourceLocation(PXLMC.MODID, "multiblock_slave").toString());
    }
}
