package ru.pearx.libmc.client.gui.structure_creation;

import jdk.internal.org.objectweb.asm.commons.TableSwitchGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Mouse;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.GuiOnScreen;
import ru.pearx.libmc.client.gui.controls.common.Button;
import ru.pearx.libmc.client.gui.controls.common.ContextMenu;
import ru.pearx.libmc.client.gui.controls.common.TextBox;
import ru.pearx.libmc.common.networking.packets.SPacketCreateStructure;
import ru.pearx.libmc.common.structure.StructureApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
            setSize(100, box.getHeight());
            setPos(box.getX() + box.getWidth(), box.getY());
        }
    }
    public static final String CACHE_FILE_NAME = "pxlmc_structure_creation_gui_cache.dat";

    public int margin = 4;
    public TextBox fromPos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button fromPosLooking;
    public TextBox toPos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button toPosLooking;
    public TextBox name = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button create = new Button(new ResourceLocation(PXLMC.MODID, "textures/gui/button.png"), I18n.format("misc.gui.structure_creation.create"), () ->
    {
        BlockPos from = parseCoords(fromPos);
        BlockPos to = parseCoords(toPos);
        if(from != null && to != null && name.getBuffer().length() > 0)
        {
            PXLMC.NETWORK.sendToServer(new SPacketCreateStructure(name.getBuffer().toString(), from, to));
        }
    });

    public GuiStructureCreation()
    {
        setWidth(386);
        setHeight(256);
        fromPos.setWidth(128);
        fromPos.setPos(margin, margin + DrawingTools.getFontHeight());
        toPos.setWidth(128);
        toPos.setPos(margin, fromPos.getY() + fromPos.getHeight() + DrawingTools.getFontHeight());
        name.setWidth(128);
        name.setPos(margin, toPos.getY() + toPos.getHeight() + DrawingTools.getFontHeight());
        create.setSize(228, 32);
        create.setPos(margin, getHeight() - create.getHeight() - margin);

        fromPosLooking = new ButtonFromLook(fromPos);
        toPosLooking = new ButtonFromLook(toPos);
    }

    @Override
    public void render()
    {
        DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), Colors.GREY_50, Colors.GREY_50);
        DrawingTools.drawString(I18n.format("misc.gui.structure_creation.fromPos"), fromPos.getX(), fromPos.getY() - DrawingTools.getFontHeight(), Colors.GREY_700);
        DrawingTools.drawString(I18n.format("misc.gui.structure_creation.toPos"), toPos.getX(), toPos.getY() - DrawingTools.getFontHeight(), Colors.GREY_700);
        DrawingTools.drawString(I18n.format("misc.gui.structure_creation.name"), name.getX(), name.getY() - DrawingTools.getFontHeight(), Colors.GREY_700);
    }

    @Override
    public void mouseDown(int button, int x, int y)
    {
        ContextMenu menu = new ContextMenu(new ContextMenu.Element("1st", () -> System.out.println("TEST")), new ContextMenu.Element("second", () -> System.out.println("TESTx2")));
        menu.setPos(getGuiScreen().getMouseX(), getGuiScreen().getMouseY());
        getOverlay().controls.add(menu);
    }

    @Override
    public void init()
    {
        controls.add(fromPos);
        controls.add(toPos);
        controls.add(fromPosLooking);
        controls.add(toPosLooking);
        controls.add(name);
        controls.add(create);

        Path p = Minecraft.getMinecraft().mcDataDir.toPath().resolve(CACHE_FILE_NAME);
        try(InputStream str = Files.newInputStream(p))
        {
            NBTTagCompound tag = CompressedStreamTools.readCompressed(str);
            if (tag.hasKey("fromPos", Constants.NBT.TAG_STRING))
                fromPos.setText(tag.getString("fromPos"));
            if (tag.hasKey("toPos", Constants.NBT.TAG_STRING))
                toPos.setText(tag.getString("toPos"));
            if(tag.hasKey("name", Constants.NBT.TAG_STRING))
                name.setText(tag.getString("name"));
        }
        catch(FileNotFoundException ignored) {}
        catch (IOException e)
        {
            PXLMC.getLog().error("An IOException occurred when loading the Structure Creation GUI cache!", e);
        }
    }

    @Override
    public void close()
    {
        try(OutputStream str = Files.newOutputStream(Minecraft.getMinecraft().mcDataDir.toPath().resolve(CACHE_FILE_NAME)))
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("fromPos", fromPos.getBuffer().toString());
            tag.setString("toPos", toPos.getBuffer().toString());
            tag.setString("name", name.getBuffer().toString());
            CompressedStreamTools.writeCompressed(tag, str);
        }
        catch (IOException e)
        {
            PXLMC.getLog().error("An IOException occurred when saving the Structure Creation GUI cache!", e);
        }
    }

    public BlockPos parseCoords(TextBox box)
    {
        try
        {
            String[] from = box.getBuffer().toString().split(" ");
            if (from.length == 3)
            {
                return new BlockPos(Integer.parseInt(from[0]), Integer.parseInt(from[0]), Integer.parseInt(from[0]));
            }
        }
        catch(NumberFormatException ex)
        {
            return null;
        }
        return null;
    }
}