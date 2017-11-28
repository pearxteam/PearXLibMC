package ru.pearx.libmc.common.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;

/*
 * Created by mrAppleXZ on 20.11.17 17:51.
 */
public class BlockUtils
{
    public static NBTTagCompound serializeBlock(IBlockState state, BlockPos relativePos, TileEntity te)
    {
        NBTTagCompound block = new NBTTagCompound();
        block.setInteger("x", relativePos.getX());
        block.setInteger("y", relativePos.getY());
        block.setInteger("z", relativePos.getZ());
        block.setString("id", state.getBlock().getRegistryName().toString());
        block.setInteger("meta", state.getBlock().getMetaFromState(state));

        if (te != null)
        {
            NBTTagCompound tiletag = te.serializeNBT();
            tiletag.removeTag("x");
            tiletag.removeTag("y");
            tiletag.removeTag("z");
            block.setTag("tile", tiletag);
        }
        return block;
    }

    public static BlockPair deserializeBlock(NBTTagCompound tag, BlockPos absPos, Map<String, Block> blockCache, World w)
    {
        String id = tag.getString("id");

        Block b;
        if(blockCache != null)
        {
            if (!blockCache.containsKey(id))
                blockCache.put(id, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id)));
            b = blockCache.get(id);
        }
        else
            b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
        IBlockState state = b.getStateFromMeta(tag.getInteger("meta"));
        if (tag.hasKey("tile"))
        {
            NBTTagCompound tile = tag.getCompoundTag("tile");
            tile.setInteger("x", absPos.getX());
            tile.setInteger("y", absPos.getY());
            tile.setInteger("z", absPos.getZ());
            return new BlockPair(state, TileEntity.create(w, tile));
        }
        return new BlockPair(state, null);
    }
}
