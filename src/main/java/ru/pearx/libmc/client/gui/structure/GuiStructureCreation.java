//this is a test piece of shitcode.
package ru.pearx.libmc.client.gui.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.PXLMC;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.GuiOnScreen;
import ru.pearx.libmc.client.gui.controls.common.Button;
import ru.pearx.libmc.client.gui.controls.common.ContextMenu;
import ru.pearx.libmc.client.gui.controls.common.ListView;
import ru.pearx.libmc.client.gui.controls.common.TextBox;
import ru.pearx.libmc.common.networking.packets.SPacketCreateStructure;
import ru.pearx.libmc.common.structure.processors.IStructureProcessor;
import ru.pearx.libmc.common.structure.processors.StructureProcessor;
import ru.pearx.libmc.common.structure.processors.StructureProcessorData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.stream.Collectors;

/*
 * Created by mrAppleXZ on 24.09.17 21:23.
 */
public class GuiStructureCreation extends GuiOnScreen
{
    public static final String CACHE_FILE_NAME = "pxlmc_structure_creation_gui_cache.dat";

    public int margin = 4;

    public LinkedList<ControlStructureProcessor> cntrls = new LinkedList<>();
    public ControlStructureProcessor cntrls_now;

    public TextBox fromPos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button fromPosLooking;
    public TextBox toPos = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button toPosLooking;
    public TextBox name = new TextBox(new ResourceLocation(PXLMC.MODID, "textures/gui/textbox.png"));
    public Button create = new Button(new ResourceLocation(PXLMC.MODID, "textures/gui/button.png"), I18n.format("misc.gui.structure.create"), () ->
    {
        BlockPos from = PXLMC.parseCoords(fromPos.getBuffer().toString());
        BlockPos to = PXLMC.parseCoords(toPos.getBuffer().toString());
        if(from != null && to != null && name.getBuffer().length() > 0)
        {
            PXLMC.NETWORK.sendToServer(new SPacketCreateStructure(name.getBuffer().toString(), from, to, cntrls.stream().map(ControlStructureProcessor::getData).collect(Collectors.toList())));
        }
    });

    public ListView procsLst = new ListView()
    {
        @Override
        public void onSelectionChanged(int old, int now)
        {
            if (now != -1)
            {
                if(cntrls_now != null)
                    GuiStructureCreation.this.getControls().remove(cntrls_now);
                cntrls_now = cntrls.get(now);
                GuiStructureCreation.this.getControls().add(cntrls.get(now));

            } else
            {
                if(cntrls_now != null)
                    GuiStructureCreation.this.getControls().remove(cntrls_now);
                cntrls_now = null;
            }
        }
    };
    public Button procsAdd = new Button(new ResourceLocation(PXLMC.MODID, "textures/gui/button.png"), "+", () ->
    {
        ContextMenu menu = new ContextMenu();
        for(IStructureProcessor proc : StructureProcessor.REGISTRY)
        {
            menu.getElements().add(new ContextMenu.Element(proc.getName(), () -> addControl(proc.getName(), proc.getControl(null))));
        }
        menu.setPos(getGuiScreen().getMouseX(), getGuiScreen().getMouseY());
        getOverlay().getControls().add(menu);
    });
    public Button procsRemove = new Button(new ResourceLocation(PXLMC.MODID, "textures/gui/button.png"), "-", () ->
    {
        int sel = procsLst.getSelection();
        if(sel != -1)
        {
            cntrls.remove(sel);
            procsLst.getElements().remove(sel);
        }
    });

    public GuiStructureCreation()
    {
        setWidth(386);
        setHeight(256);
        fromPos.setWidth(128);
        fromPos.setPos(margin, margin + DrawingTools.getFontHeight());
        toPos.setWidth(128);
        toPos.setPos(margin, fromPos.getY() + fromPos.getHeight() * 2 + DrawingTools.getFontHeight());
        name.setWidth(128);
        name.setPos(margin, toPos.getY() + toPos.getHeight() * 2+ DrawingTools.getFontHeight());
        create.setSize(128, 16);
        create.setPos(margin, getHeight() - create.getHeight() - margin);

        procsLst.setPos(margin, name.getY() + name.getHeight() + margin);
        procsLst.setSize(128, 118);
        procsAdd.setPos(procsLst.getX(), procsLst.getY() + procsLst.getHeight());
        procsAdd.setSize(64, 16);
        procsRemove.setPos(procsAdd.getX() + 64, procsAdd.getY());
        procsRemove.setSize(64, 16);

        fromPosLooking = new ButtonFromLook(fromPos);
        toPosLooking = new ButtonFromLook(toPos);
    }

    public void addControl(String name, ControlStructureProcessor cont)
    {
        cont.setPos(create.getX() + create.getWidth() + margin, margin);
        cntrls.add(cont);
        procsLst.getElements().add(name);
    }

    @Override
    public void render()
    {
        DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), Colors.GREY_700);
        DrawingTools.drawString(I18n.format("misc.gui.structure.fromPos"), fromPos.getX(), fromPos.getY() - DrawingTools.getFontHeight(), Colors.WHITE);
        DrawingTools.drawString(I18n.format("misc.gui.structure.toPos"), toPos.getX(), toPos.getY() - DrawingTools.getFontHeight(), Colors.WHITE);
        DrawingTools.drawString(I18n.format("misc.gui.structure.name"), name.getX(), name.getY() - DrawingTools.getFontHeight(), Colors.WHITE);
    }

    @Override
    public void init()
    {
        getControls().add(fromPos);
        getControls().add(toPos);
        getControls().add(fromPosLooking);
        getControls().add(toPosLooking);
        getControls().add(name);
        getControls().add(create);
        getControls().add(procsLst);
        getControls().add(procsAdd);
        getControls().add(procsRemove);

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
            if(tag.hasKey("processors", Constants.NBT.TAG_LIST))
            {
                NBTTagList lst = tag.getTagList("processors", Constants.NBT.TAG_COMPOUND);
                for(NBTBase nbt : lst)
                {
                    NBTTagCompound proc = (NBTTagCompound) nbt;
                    IStructureProcessor process = StructureProcessor.REGISTRY.getValue(new ResourceLocation(proc.getString("id")));
                    BlockPos pos = proc.hasKey("x", Constants.NBT.TAG_INT) && proc.hasKey("y", Constants.NBT.TAG_INT) && proc.hasKey("z", Constants.NBT.TAG_INT) ? new BlockPos(proc.getInteger("x"), proc.getInteger("y"), proc.getInteger("z")) : null;
                    addControl(process.getName(), process.getControl(process.loadData(proc.getCompoundTag("data"), pos)));
                }
            }
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
            NBTTagList lst = new NBTTagList();
            for (ControlStructureProcessor cont : cntrls)
            {
                Pair<ResourceLocation, StructureProcessorData> p = cont.getData();
                NBTTagCompound proc = new NBTTagCompound();
                proc.setString("id", p.getLeft().toString());
                if(p.getRight().getAbsolutePos() != null)
                {
                    proc.setInteger("x", p.getRight().getAbsolutePos().getX());
                    proc.setInteger("y", p.getRight().getAbsolutePos().getY());
                    proc.setInteger("z", p.getRight().getAbsolutePos().getZ());
                }
                proc.setTag("data", p.getRight().serialize());
                lst.appendTag(proc);
            }
            tag.setTag("processors", lst);
            CompressedStreamTools.writeCompressed(tag, str);
        }
        catch (IOException e)
        {
            PXLMC.getLog().error("An IOException occurred when saving the Structure Creation GUI cache!", e);
        }
    }
}