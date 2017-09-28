package ru.pearx.libmc.client.gui.structure;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.Control;
import ru.pearx.libmc.client.gui.controls.common.TextBox;
import ru.pearx.libmc.common.structure.StructureProcessorData;

/*
 * Created by mrAppleXZ on 27.09.17 20:34.
 */
public abstract class ControlStructureProcessor extends Control
{
    public TextBox pos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public ButtonFromLook fromLook;

    public ControlStructureProcessor()
    {
        setSize(250, 248);
        pos.setPos(0, DrawingTools.getFontHeight());
        pos.setWidth(64);
        fromLook = new ButtonFromLook(pos);
    }

    @Override
    public void render()
    {
        DrawingTools.drawString(I18n.format("misc.gui.structure.pos"), 0, 0, Colors.WHITE);
    }

    @Override
    public void init()
    {
        controls.add(pos);
        controls.add(fromLook);
    }

    public void setPosText(BlockPos pos)
    {
        if(pos != null)
            this.pos.setText(pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }

    public abstract Pair<ResourceLocation, StructureProcessorData> getData();
}
