package ru.pearx.libmc.common.structure.processors;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.common.LegacyListView;
import ru.pearx.libmc.client.gui.controls.common.TextBox;
import ru.pearx.libmc.client.gui.structure.ControlStructureProcessor;

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
        public LegacyListView faces = new LegacyListView();

        public LootControl()
        {
            table.setWidth(getWidth() - pos.getWidth());
            table.setPos(pos.getX() + pos.getWidth(), pos.getY());
            table.setMaxRenderLength(48);

            faces.setSize(128, 128);
            faces.setPos(0, fromLook.getY() + fromLook.getHeight() + 6);

            for(EnumFacing face : EnumFacing.VALUES)
            {
                faces.getElements().add(face.getName().toUpperCase());
            }
        }

        public LootControl(Data data)
        {
            this();
            setPosText(data.getAbsolutePos());
            table.setText(data.table.toString());
            faces.setSelection(data.facing.getIndex());
        }

        @Override
        public Pair<ResourceLocation, StructureProcessorData> getData()
        {
            return Pair.of(ID, new Data(PXLMC.parseCoords(pos.getBuffer().toString()), new ResourceLocation(table.getBuffer().toString()), EnumFacing.VALUES[faces.getSelection() >= 0 ? faces.getSelection() : 0]));
        }

        @Override
        public void render()
        {
            super.render();
            DrawingTools.drawString(I18n.format("misc.structure_processor.loot.table"), table.getX(), 0, Colors.WHITE);
        }

        @Override
        public void init()
        {
            super.init();
            getControls().add(table);
            getControls().add(faces);
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
        PXLMC.fillBlockWithLoot(world, rand, d.getAbsolutePos(), d.facing, d.table, 0);
    }

    @Override
    public StructureProcessorData loadData(NBTTagCompound tag, BlockPos pos)
    {
        Data dat = new Data(pos);
        dat.deserialize(tag);
        return dat;
    }
}
