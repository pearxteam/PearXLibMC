package ru.pearx.libmc.client.gui.controls;

import net.minecraft.client.gui.inventory.GuiContainer;

/*
 * Created by mrAppleXZ on 08.11.17 12:41.
 */
public class GuiOnScreenContainer extends GuiOnScreen
{
    private GuiContainer cont;
    public GuiOnScreenContainer(GuiContainer cont)
    {
        this.cont = cont;
    }

    @Override
    public int getWidth()
    {
        return cont.getXSize();
    }

    @Override
    public int getHeight()
    {
        return cont.getYSize();
    }
}
