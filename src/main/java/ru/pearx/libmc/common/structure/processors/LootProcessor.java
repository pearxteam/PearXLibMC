package ru.pearx.libmc.common.structure.processors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.IForgeRegistryEntry;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.common.structure.IStructureProcessor;
import ru.pearx.libmc.common.structure.StructureProcessorData;

import java.util.List;
import java.util.Random;

/*
 * Created by mrAppleXZ on 24.09.17 20:59.
 */
public class LootProcessor extends IForgeRegistryEntry.Impl<IStructureProcessor> implements IStructureProcessor
{
    public static ResourceLocation ID = new ResourceLocation(PXLMC.MODID, "loot");

    public static class Data extends StructureProcessorData
    {
        public ResourceLocation table;
        public EnumFacing facing;

        public Data(BlockPos pos, ResourceLocation table, EnumFacing facing)
        {
            setAbsolutePos(pos);
            this.table = table;
            this.facing = facing;
        }

        public Data(BlockPos pos)
        {
            setAbsolutePos(pos);
        }

        @Override
        public NBTTagCompound serialize()
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("table", table.toString());
            tag.setInteger("facing", facing.getIndex());
            return tag;
        }

        @Override
        public void deserialize(NBTTagCompound tag)
        {
            table = new ResourceLocation(tag.getString("table"));
            facing = EnumFacing.values()[tag.getInteger("facing")];
        }

        @Override
        public ResourceLocation getProcessorId()
        {
            return ID;
        }
    }

    @Override
    public void process(StructureProcessorData data, WorldServer world, Random rand)
    {
        if(data instanceof Data)
        {
            Data d = (Data) data;
            TileEntity te = world.getTileEntity(d.getAbsolutePos());
            if(te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.facing) instanceof IItemHandlerModifiable)
            {
                IItemHandlerModifiable hand = (IItemHandlerModifiable)te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.facing);
                LootTable table = world.getLootTableManager().getLootTableFromLocation(d.table);
                List<ItemStack> items = table.generateLootForPools(rand, new LootContext(0, world, world.getLootTableManager(), null, null, null));
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

    @Override
    public StructureProcessorData loadData(BlockPos pos, NBTTagCompound tag)
    {
        Data d = new Data(pos);
        d.deserialize(tag);
        return d;
    }
}
