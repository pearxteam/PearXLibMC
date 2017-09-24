package ru.pearx.libmc.client.gui.structure_creation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.GuiOnScreen;
import ru.pearx.libmc.client.gui.controls.common.Button;
import ru.pearx.libmc.client.gui.controls.common.TextBox;

/*
 * Created by mrAppleXZ on 24.09.17 21:23.
 */
public class GuiStructureCreation extends GuiOnScreen
{
    public class ButtonFromLook extends Button
    {
        public ButtonFromLook(TextBox box)
        {
            super(new ResourceLocation(PXLMC.MODID, "textures/gui/button.png"), I18n.format("misc.gui.structure_creation.looking_at"), () ->
            {
                BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
                box.setText(pos.getX() + " " + pos.getY() + " " + pos.getZ());
            });
            setSize(64, box.getHeight());
            setPos(box.getX() + box.getWidth(), box.getY());
        }
    }
    public TextBox fromPos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button fromPosLooking = new ButtonFromLook(fromPos);
    public TextBox toPos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button toPosLooking;

    public GuiStructureCreation()
    {
        setWidth(386);
        setHeight(256);
        fromPos.setWidth(128);
        fromPos.setPos(0, DrawingTools.getFontHeight());
        toPos.setWidth(128);
        toPos.setPos(0, fromPos.getY() + fromPos.getHeight() + DrawingTools.getFontHeight());

        fromPosLooking = new ButtonFromLook(fromPos);
        toPosLooking = new ButtonFromLook(toPos);
    }

    @Override
    public void render()
    {
        DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), Colors.GREY_50, Colors.GREY_50);
        DrawingTools.drawString(I18n.format("misc.gui.structure_creation.fromPos"), 0, 0, Colors.GREY_700);
        DrawingTools.drawString(I18n.format("misc.gui.structure_creation.toPos"), 0, fromPos.getHeight(), Colors.GREY_700);
    }

    @Override
    public void init()
    {
        controls.add(fromPos);
        controls.add(toPos);
        controls.add(fromPosLooking);
        controls.add(toPosLooking);
    }
}