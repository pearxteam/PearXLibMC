package ru.pearx.libmc.common.structure.blockarray;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.pearx.lib.collections.EventMap;
import ru.pearx.libmc.PXLMC;

import java.util.*;

/*
 * Created by mrAppleXZ on 15.10.17 16:52.
 */
public class BlockArray
{
    public interface Checker
    {
        boolean check(BlockArrayEntry entr, World w, BlockPos pos, Rotation rot);
    }
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
    private List<Checker> checkers = new ArrayList<>();

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

    public List<Checker> getCheckers()
    {
        return checkers;
    }

    public boolean check(World w, BlockPos zeroPos, Rotation rot)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(zeroPos);
        BlockPos.MutableBlockPos relPos = new BlockPos.MutableBlockPos();
        for(Map.Entry<BlockPos, BlockArrayEntry> entr : getMap().entrySet())
        {
            relPos.setPos(entr.getKey().getX(), entr.getKey().getY(), entr.getKey().getZ());
            relPos = PXLMC.transformPos(relPos, null, rot);
            pos.setPos(relPos.getX() + zeroPos.getX(), relPos.getY() + zeroPos.getY(), relPos.getZ() + zeroPos.getZ());
            if(!checkEntry(entr.getValue(), w, pos, rot))
            {
                return false;
            }
        }
        return true;
    }

    public Optional<Rotation> check(World w, BlockPos zeroPos)
    {
        for(Rotation rot : Rotation.values())
        {
            if(check(w, zeroPos, rot))
                return Optional.of(rot);
        }
        return Optional.empty();
    }

    public boolean checkEntry(BlockArrayEntry entr, World w, BlockPos pos, Rotation rot)
    {
        IBlockState st = entr.getState().withRotation(rot);
        IBlockState wst = w.getBlockState(pos);
        if(wst.getBlock() != st.getBlock())
            return false;
        for(IProperty<?> prop : st.getPropertyKeys())
        {
            if(!st.getValue(prop).equals(wst.getValue(prop)))
                return false;
        }
        return performAdditionalChecks(entr, w, pos, rot);
    }

    public boolean performAdditionalChecks(BlockArrayEntry entr, World w, BlockPos pos, Rotation rot)
    {
        return getCheckers().parallelStream().noneMatch((ch) -> ch.check(entr, w, pos, rot));
    }
}
