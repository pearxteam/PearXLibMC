package ru.pearx.libmc.common.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.lib.Size;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.blocks.PXLBlocks;

import javax.vecmath.Vector3f;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

/*
 * Created by mrAppleXZ on 20.08.17 23:19.
 */
public class StructureApi
{
    public static class StructureLootEntry
    {
        private BlockPos pos;
        private EnumFacing facing;
        private ResourceLocation table;

        public StructureLootEntry(BlockPos pos, EnumFacing facing, ResourceLocation table)
        {
            this.pos = pos;
            this.facing = facing;
            this.table = table;
        }

        public StructureLootEntry()
        {
        }

        public BlockPos getPos()
        {
            return pos;
        }

        public void setPos(BlockPos pos)
        {
            this.pos = pos;
        }

        public EnumFacing getFacing()
        {
            return facing;
        }

        public void setFacing(EnumFacing facing)
        {
            this.facing = facing;
        }

        public ResourceLocation getTable()
        {
            return table;
        }

        public void setTable(ResourceLocation table)
        {
            this.table = table;
        }
    }

    public static void createStructure(String name, BlockPos from, BlockPos to, World world, StructureLootEntry... loot)
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
        BlockPos size = to.subtract(from);
        root.setInteger("sizeX", size.getX());
        root.setInteger("sizeY", size.getY());
        root.setInteger("sizeZ", size.getZ());
        BlockPos centerPos = new BlockPos((to.getX() - from.getX()) / 2 + from.getX(), (to.getY() - from.getY()) / 2 + from.getY(), (to.getZ() - from.getZ()) / 2 + from.getZ());

        {
            NBTTagList blocks = new NBTTagList();
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos relativePos = new BlockPos.MutableBlockPos();
            for (int x = from.getX(); x <= to.getX(); x++)
            {
                for (int y = from.getY(); y <= to.getY(); y++)
                {
                    for (int z = from.getZ(); z <= to.getZ(); z++)
                    {
                        pos.setPos(x, y, z);
                        relativePos.setPos(pos.getX() - centerPos.getX(), pos.getY() - centerPos.getY(), pos.getZ() - centerPos.getZ());

                        IBlockState state = world.getBlockState(pos);
                        if (state.getBlock() == PXLBlocks.structure_nothing)
                            continue;
                        NBTTagCompound block = new NBTTagCompound();
                        block.setInteger("x", relativePos.getX());
                        block.setInteger("y", relativePos.getY());
                        block.setInteger("z", relativePos.getZ());//
                        block.setString("id", state.getBlock().getRegistryName().toString());
                        block.setInteger("meta", state.getBlock().getMetaFromState(state));

                        TileEntity te = world.getTileEntity(pos);
                        if (te != null)
                        {
                            NBTTagCompound tiletag = te.serializeNBT();
                            tiletag.removeTag("x");
                            tiletag.removeTag("y");
                            tiletag.removeTag("z");
                            block.setTag("tile", tiletag);
                        }

                        blocks.appendTag(block);
                    }
                }
            }
            root.setTag("blocks", blocks);
        }
        {
            NBTTagList entities = new NBTTagList();
            for(Entity e : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(from, to.add(1, 1, 1))))
            {
                if(!(e instanceof EntityPlayer))
                {
                    NBTTagCompound tag = e.serializeNBT();
                    NBTTagList pos = tag.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
                    pos.set(0, new NBTTagDouble(pos.getDoubleAt(0) - centerPos.getX()));
                    pos.set(1, new NBTTagDouble(pos.getDoubleAt(1) - centerPos.getY()));
                    pos.set(2, new NBTTagDouble(pos.getDoubleAt(2) - centerPos.getZ()));
                    tag.removeTag("Dimension");
                    tag.removeTag("UUIDMost");
                    tag.removeTag("UUIDLeast");
                    if(tag.hasKey("TileX", Constants.NBT.TAG_INT))
                        tag.setInteger("TileX", tag.getInteger("TileX") - centerPos.getX());
                    if(tag.hasKey("TileY", Constants.NBT.TAG_INT))
                        tag.setInteger("TileY", tag.getInteger("TileY") - centerPos.getY());
                    if(tag.hasKey("TileZ", Constants.NBT.TAG_INT))
                        tag.setInteger("TileZ", tag.getInteger("TileZ") - centerPos.getZ());
                    entities.appendTag(tag);
                }
            }
            root.setTag("entities", entities);
        }
        {
            NBTTagList lst = new NBTTagList();
            for(StructureLootEntry e : loot)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("x", e.getPos().getX() - centerPos.getX());
                tag.setInteger("y", e.getPos().getY() - centerPos.getY());
                tag.setInteger("z", e.getPos().getZ() - centerPos.getZ());
                tag.setString("table", e.getTable().toString());
                tag.setInteger("facing", e.getFacing().getIndex());
                lst.appendTag(tag);
            }
            root.setTag("loot", lst);
        }

        Path p = Paths.get("pxlmc_structures", name + ".dat");
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

    public static NBTTagCompound getStructureNbt(String fileName) throws IOException
    {
        try(InputStream str = Files.newInputStream(Paths.get("pxlmc_structures", fileName + ".dat")))
        {
            return CompressedStreamTools.readCompressed(str);
        }
    }

    public static Vec3i getStructureSize(NBTTagCompound tag)
    {
        return new Vec3i(tag.getInteger("sizeX"), tag.getInteger("sizeY"), tag.getInteger("sizeZ"));
    }

    public static void spawnStructure(NBTTagCompound tag, BlockPos at, WorldServer w, Random rand)
    {
        {
            NBTTagList blocks = tag.getTagList("blocks", Constants.NBT.TAG_COMPOUND);
            BlockPos.MutableBlockPos absPos = new BlockPos.MutableBlockPos();
            Map<String, Block> bcache = new HashMap<>();
            for (NBTBase base : blocks)
            {
                NBTTagCompound block = (NBTTagCompound) base;
                absPos.setPos(at.getX() + block.getInteger("x"), at.getY() + block.getInteger("y"), at.getZ() + block.getInteger("z"));
                String id = block.getString("id");
                if(!bcache.containsKey(id))
                    bcache.put(id, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id)));
                Block b = bcache.get(id);
                IBlockState state = b.getStateFromMeta(block.getInteger("meta"));
                w.setBlockState(absPos, state, 2);
                if (block.hasKey("tile"))
                {
                    NBTTagCompound tile = block.getCompoundTag("tile");
                    tile.setInteger("x", absPos.getX());
                    tile.setInteger("y", absPos.getY());
                    tile.setInteger("z", absPos.getZ());
                    w.setTileEntity(absPos, TileEntity.create(w, tile));
                }
            }
        }
        {
            NBTTagList entities = tag.getTagList("entities", Constants.NBT.TAG_COMPOUND);
            for(NBTBase nbt : entities)
            {
                NBTTagCompound entity = (NBTTagCompound) nbt;
                entity.setInteger("Dimension", w.provider.getDimension());
                NBTTagList pos = entity.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
                pos.set(0, new NBTTagDouble(pos.getDoubleAt(0) + at.getX()));
                pos.set(1, new NBTTagDouble(pos.getDoubleAt(1) + at.getY()));
                pos.set(2, new NBTTagDouble(pos.getDoubleAt(2) + at.getZ()));
                if(entity.hasKey("TileX", Constants.NBT.TAG_INT))
                    entity.setInteger("TileX", entity.getInteger("TileX") + at.getX());
                if(entity.hasKey("TileY", Constants.NBT.TAG_INT))
                    entity.setInteger("TileY", entity.getInteger("TileY") + at.getY());
                if(entity.hasKey("TileZ", Constants.NBT.TAG_INT))
                    entity.setInteger("TileZ", entity.getInteger("TileZ") + at.getZ());

                Entity e = EntityList.createEntityFromNBT(entity, w);
                if(e != null)
                    w.spawnEntity(e);
            }
        }
        {
            NBTTagList loots = tag.getTagList("loot", Constants.NBT.TAG_COMPOUND);
            for(NBTBase nbt : loots)
            {
                NBTTagCompound loot = (NBTTagCompound) nbt;
                BlockPos pos = new BlockPos(loot.getInteger("x") + at.getX(), loot.getInteger("y") + at.getY(), loot.getInteger("z") + at.getZ());
                EnumFacing face = EnumFacing.getFront(loot.getInteger("facing"));
                TileEntity te = w.getTileEntity(pos);
                if(te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face) instanceof IItemHandlerModifiable)
                {
                    IItemHandlerModifiable hand = (IItemHandlerModifiable)te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
                    LootTable table = w.getLootTableManager().getLootTableFromLocation(new ResourceLocation(loot.getString("table")));
                    List<ItemStack> items = table.generateLootForPools(rand, new LootContext(0, w, w.getLootTableManager(), null, null, null));
                    for(int i = 0; i < hand.getSlots(); i++)
                    {
                        if (items.size() <= 0)
                            break;
                        int index = rand.nextInt(items.size());
                        hand.setStackInSlot(i, items.get(index));
                        items.remove(index);
                    }
                }
            }
        }
    }
}
