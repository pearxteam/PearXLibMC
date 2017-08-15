package ru.pearx.libmc.client.gui.drawables;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.IGuiScreen;

/**
 * Created by mrAppleXZ on 15.04.17 9:48.
 */
@SideOnly(Side.CLIENT)
public interface IGuiDrawable
{
    int getWidth();
    int getHeight();

    void draw(IGuiScreen screen, int x, int y);
}
