package ru.pearx.libmc.common.structure.blockarray;

import net.minecraft.util.math.BlockPos;
import ru.pearx.lib.collections.EventMap;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by mrAppleXZ on 15.10.17 16:52.
 */
public class BlockArray
{
    private int minX, maxX, minY, maxY, minZ, maxZ;
    private EventMap<BlockPos, BlockArrayEntry> map = new EventMap<>(new HashMap<>(), () ->
    {
        BlockPos p = getMap().keySet().iterator().next();
        minX = p.getX();
        maxX = p.getX();
        minY = p.getY();
        maxY = p.getY();
        minZ = p.getZ();
        maxZ = p.getZ();
        for(BlockPos pos : getMap().keySet())
        {
            if(pos.getX() < minX)
                minX = pos.getX();
            if(pos.getX() > maxX)
                maxX = pos.getX();
            if(pos.getY() < minY)
                minY = pos.getY();
            if(pos.getY() > maxY)
                maxY = pos.getY();
            if(pos.getZ() < minZ)
                minZ = pos.getZ();
            if(pos.getZ() > maxZ)
                maxZ = pos.getZ();
        }
    });

    public BlockArray()
    {

    }

    public int getMinX()
    {
        return minX;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public int getMinY()
    {
        return minY;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public int getMinZ()
    {
        return minZ;
    }

    public int getMaxZ()
    {
        return maxZ;
    }

    public Map<BlockPos, BlockArrayEntry> getMap()
    {
        return map;
    }
}
