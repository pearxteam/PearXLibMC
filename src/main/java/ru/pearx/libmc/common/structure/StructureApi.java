package ru.pearx.libmc.common.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.launcher.FMLTweaker;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.blocks.PXLBlocks;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * Created by mrAppleXZ on 20.08.17 23:19.
 */
public class StructureApi
{
    public static void createStructure(String name, BlockPos from, BlockPos to, World world)
    {
        int frx = from.getX(), fry = from.getY(), frz = from.getZ(), tx = to.getX(), ty = to.getY(), tz = to.getZ();
        if (from.getX() > to.getX())
        {
            frx = to.getX();
            tx = from.getX();
        }
        if (from.getY() > to.getY())
        {
            fry = to.getY();
            ty = from.getY();
        }
        if (from.getZ() > to.getZ())
        {
            frz = to.getZ();
            tz = from.getZ();
        }
        from = new BlockPos(frx, fry, frz);
        to = new BlockPos(tx, ty, tz);

        NBTTagCompound root = new NBTTagCompound();
        NBTTagList blocks = new NBTTagList();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos centerPos = new BlockPos((to.getX() - from.getX()) / 2 + from.getX(), (to.getY() - from.getY()) / 2 + from.getY(), (to.getZ() - from.getZ()) / 2 + from.getZ());
        BlockPos.MutableBlockPos relativePos = new BlockPos.MutableBlockPos();
        for(int x = from.getX(); x <= to.getX(); x++)
        {
            for(int y = from.getY(); y <= to.getY(); y++)
            {
                for(int z = from.getZ(); z <= to.getZ(); z++)
                {
                    pos.setPos(x, y, z);
                    relativePos.setPos(pos.getX() - centerPos.getX(), pos.getY() - centerPos.getY(), pos.getZ() - centerPos.getZ());
                    IBlockState state = world.getBlockState(pos);
                    if(state.getBlock() == PXLBlocks.structure_nothing)
                        break;
                    NBTTagCompound block = new NBTTagCompound();
                    block.setInteger("x", relativePos.getX());
                    block.setInteger("y", relativePos.getY());
                    block.setInteger("z", relativePos.getZ());
                    block.setString("id", state.getBlock().getRegistryName().toString());
                    block.setInteger("meta", state.getBlock().getMetaFromState(state));
                    blocks.appendTag(block);
                }
            }
        }
        root.setTag("blocks", blocks);

        Path p = Paths.get(".", "pxlmc_structures", name + ".dat");
        if(Files.notExists(p.getParent()))
            try
            {
                Files.createDirectory(p.getParent());
            }
            catch (IOException e)
            {
                PXLMC.getLog().error("Can't create " + p.getParent() + " directory!", e);
            }
        try(OutputStream str = Files.newOutputStream(p))
        {
            CompressedStreamTools.writeCompressed(root, str);
        }
        catch (IOException e)
        {
            PXLMC.getLog().error("An IOException occurred when creating the " + p + " structure file!", e);
        }

    }
}
