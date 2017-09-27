package ru.pearx.libmc.client.gui.controls;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.IGuiScreen;

/*
 * Created by mrAppleXZ on 27.09.17 16:00.
 */
@SideOnly(Side.CLIENT)
public interface IGuiScreenProvider
{
    IGuiScreen getGs();

    void setGs(IGuiScreen gs);
}
