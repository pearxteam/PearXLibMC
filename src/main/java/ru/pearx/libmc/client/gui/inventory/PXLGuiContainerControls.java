package ru.pearx.libmc.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.IGuiScreen;
import ru.pearx.libmc.client.gui.SharedGuiMethods;
import ru.pearx.libmc.client.gui.controls.Control;
import ru.pearx.libmc.client.gui.controls.GuiControlContainer;

import java.io.IOException;

/*
 * Created by mrAppleXZ on 17.09.17 15:53.
 */
@SideOnly(Side.CLIENT)
public class PXLGuiContainerControls extends PXLGuiContainer implements IGuiScreen
{
    public GuiControlContainer gui;

    public PXLGuiContainerControls(Container cont, GuiControlContainer cg)
    {
        super(cont);
        gui = cg;
    }

    public PXLGuiContainerControls(Container cont, Control contr)
    {
        super(cont);
        gui = new GuiControlContainer(contr, this);
    }

    public PXLGuiContainerControls(Container cont)
    {
        super(cont);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-guiLeft, -guiTop, 0);
        if(gui != null)
        {
            gui.invokeRender(0);
            gui.invokeRender2();
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void handleKeyboardInput() throws IOException
    {
        super.handleKeyboardInput();

        SharedGuiMethods.handleKeyboardInput(gui);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        SharedGuiMethods.keyTyped( gui, typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        SharedGuiMethods.handleMouseInput(this, gui);
    }

    @Override
    public void drawTooltip(ItemStack stack, int x, int y)
    {
        SharedGuiMethods.drawTooltip(stack, x, y, this::renderToolTip);
    }

    @Override
    public void drawHovering(String text, int x, int y)
    {
        SharedGuiMethods.drawHovering(text, x, y, this::drawHoveringText);
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int getMouseX()
    {
        return SharedGuiMethods.getMouseX(this);
    }

    @Override
    public int getMouseY()
    {
        return SharedGuiMethods.getMouseY(this);
    }

    @Override
    public RenderItem getRenderItem()
    {
        return itemRender;
    }

    @Override
    public void onGuiClosed()
    {
        gui.invokeClose();
    }

    public void setControl(Control cont)
    {
        gui = new GuiControlContainer(cont, this);
    }

    public void setControlContainer(GuiControlContainer cont)
    {
        gui = cont;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
        gui.invokeInit();
    }

    @Override
    public void updateScreen()
    {
        gui.invokeUpdate();
    }

    @Override
    public void close()
    {
        if(Minecraft.getMinecraft().currentScreen == this)
            Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
