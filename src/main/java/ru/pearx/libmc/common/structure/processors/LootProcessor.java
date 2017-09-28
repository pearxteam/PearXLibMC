package ru.pearx.libmc.common.structure.processors;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.common.TextBox;
import ru.pearx.libmc.client.gui.structure.ControlStructureProcessor;
import ru.pearx.libmc.common.structure.StructureProcessorData;

import java.util.List;
import java.util.Random;

/*
 * Created by mrAppleXZ on 24.09.17 20:59.
 */
public class LootProcessor extends StructureProcessor
{
    public static ResourceLocation ID = new ResourceLocation(PXLMC.MODID, "loot");

    public LootProcessor()
    {
        setRegistryName(ID);
    }

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
    }

    public static class LootControl extends ControlStructureProcessor
    {
        public TextBox table = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));

        public LootControl()
        {
            table.setWidth(getWidth() - pos.getWidth());
            table.setPos(pos.getX() + pos.getWidth(), pos.getY());
        }

        public LootControl(Data data)
        {
            this();
            setPosText(data.getAbsolutePos());
            table.setText(data.table.toString());
        }

        @Override
        public Pair<ResourceLocation, StructureProcessorData> getData()
        {
            //fixme enumfacing.east
            return Pair.of(ID, new Data(PXLMC.parseCoords(pos.getBuffer().toString()), new ResourceLocation(table.getBuffer().toString()), EnumFacing.EAST));
        }

        @Override
        public void render()
        {
            super.render();
            DrawingTools.drawString("misc.structure_processor.loot.table", table.getX(), 0, Colors.WHITE);
        }

        @Override
        public void init()
        {
            super.init();
            controls.add(table);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ControlStructureProcessor getControl(StructureProcessorData data)
    {
        if(data != null)
        {
            Data dat = (Data) data;
            return new LootControl(dat);
        }
        return new LootControl();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getName()
    {
        return I18n.format("misc.structure_processor.loot.name");
    }

    @Override
    public void process(StructureProcessorData data, WorldServer world, Random rand)
    {
        Data d = (Data) data;

        TileEntity te = world.getTileEntity(d.getAbsolutePos());
        if (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.facing) instanceof IItemHandlerModifiable)
        {
            IItemHandlerModifiable hand = (IItemHandlerModifiable) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.facing);
            LootTable table = world.getLootTableManager().getLootTableFromLocation(d.table);
            List<ItemStack> items = table.generateLootForPools(rand, new LootContext(0, world, world.getLootTableManager(), null, null, null));
            for (int i = 0; i < hand.getSlots(); i++)
            {
                if (items.size() <= 0)
                    break;
                int index = rand.nextInt(items.size());
                hand.setStackInSlot(i, items.get(index));
                items.remove(index);
            }
        }
    }

    @Override
    public StructureProcessorData loadData(NBTTagCompound tag, BlockPos pos)
    {
        Data dat = new Data(pos);
        dat.deserialize(tag);
        return dat;
    }
}
