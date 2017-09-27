package ru.pearx.libmc.client.gui.controls;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by mrAppleXZ on 27.09.17 16:42.
 */
@SideOnly(Side.CLIENT)
public interface IOverlayProvider
{
    GuiControlContainer.OverlayContainer getOverlay();
}
