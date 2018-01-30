package ru.pearx.libmc.client.gui.controls.common;

import org.lwjgl.input.Mouse;
import ru.pearx.libmc.client.gui.controls.Control;

/*
 * Created by mrAppleXZ on 16.10.17 20:14.
 */
public abstract class AbstractShowcase extends Control
{
    protected int rotX, rotY;
    protected int scale;
    private int prevMouseX, prevMouseY;

    @Override
    public void mouseMove(int x, int y, int dx, int dy)
    {
        if(Mouse.isButtonDown(0))
        {
            int mX = x - prevMouseX;
            int mY = y - prevMouseY;
            rotY += mX;
            rotX += mY;
            if(rotX > 360)
                rotX -= 360;
            if(rotY > 360)
                rotY -= 360;
            if(rotX < 0)
                rotX += 360;
            if(rotY < 0)
                rotY += 360;
        }
        prevMouseX = x;
        prevMouseY = y;
    }
    @Override
    public void mouseWheel(int delta)
    {
        if (isFocused())
        {
            scale += delta / 120;
            if (scale < 0)
                scale = 0;
        }
    }

    @Override
    public boolean shouldStencil()
    {
        return true;
    }
}
