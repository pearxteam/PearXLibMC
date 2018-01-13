package ru.pearx.libmc.client.gui.controls;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.IGuiScreen;

/**
 * Created by mrAppleXZ on 16.04.17 19:52.
 */
@SideOnly(Side.CLIENT)
public class GuiControlContainer extends Control implements IGuiScreenProvider, IOverlayProvider
{
    @SideOnly(Side.CLIENT)
    public class OverlayContainer extends Control implements IGuiScreenProvider, IOverlayProvider
    {
        public OverlayContainer()
        {
            initialized = true;
        }

        @Override
        public IGuiScreen getGs()
        {
            return GuiControlContainer.this.getGs();
        }

        @Override
        public void setGs(IGuiScreen gs)
        {
            GuiControlContainer.this.setGs(gs);
        }

        @Override
        public int getWidth()
        {
            return GuiControlContainer.this.getWidth();
        }

        @Override
        public int getHeight()
        {
            return GuiControlContainer.this.getHeight();
        }

        public boolean isActive()
        {
            return controls.size() > 0;
        }

        @Override
        public OverlayContainer getOverlay()
        {
            return this;
        }

        @Override
        public void mouseUp(int button, int x, int y)
        {
            controls.clear();
        }
    }

    private IGuiScreen gs;
    private OverlayContainer overlay = new OverlayContainer();
    private Control cont;
    public GuiControlContainer(Control cont, IGuiScreen gs)
    {
        setGs(gs);
        this.cont = cont;
    }

    @Override
    public void init()
    {
        controls.add(cont);
    }

    @Override
    public int getWidth()
    {
        return getGuiScreen().getWidth();
    }

    @Override
    public int getHeight()
    {
        return getGuiScreen().getHeight();
    }


    @Override
    public IGuiScreen getGs()
    {
        return gs;
    }

    @Override
    public void setGs(IGuiScreen gs)
    {
        this.gs = gs;
    }

    @Override
    public void invokeClose()
    {
        super.invokeClose();
        overlay.invokeClose();
    }

    @Override
    public void invokeKeyDown(int keycode)
    {
        if(overlay.isActive())
            overlay.invokeKeyDown(keycode);
        else
            super.invokeKeyDown(keycode);
    }

    @Override
    public void invokeKeyUp(int keycode)
    {
        if(overlay.isActive())
            overlay.invokeKeyUp(keycode);
        else
            super.invokeKeyUp(keycode);
    }

    @Override
    public void invokeKeyPress(char key, int keycode)
    {
        if(overlay.isActive())
            overlay.invokeKeyPress(key, keycode);
        else
            super.invokeKeyPress(key, keycode);
    }

    @Override
    public void invokeMouseDown(int button, int x, int y)
    {
        if(overlay.isActive())
            overlay.invokeMouseDown(button, x, y);
        else
            super.invokeMouseDown(button, x, y);
    }

    @Override
    public void invokeMouseUp(int button, int x, int y)
    {
        if(overlay.isActive())
            overlay.invokeMouseUp(button, x, y);
        else
            super.invokeMouseUp(button, x, y);
    }

    @Override
    public void invokeMouseMove(int x, int y, int dx, int dy)
    {
        if(overlay.isActive())
            overlay.invokeMouseMove(x, y, dx, dy);
        else
            super.invokeMouseMove(x, y, dx, dy);
    }

    @Override
    public void invokeMouseWheel(int delta)
    {
        if(overlay.isActive())
            overlay.invokeMouseWheel(delta);
        else
            super.invokeMouseWheel(delta);
    }

    @Override
    public void invokeRender()
    {
        if(overlay.isActive())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 150);
            overlay.invokeRender();
            GlStateManager.popMatrix();
        }
        super.invokeRender();
    }

    @Override
    public void invokeRender2()
    {
        if(overlay.isActive())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 150);
            overlay.invokeRender2();
            GlStateManager.popMatrix();
        }
        super.invokeRender2();
    }

    @Override
    public OverlayContainer getOverlay()
    {
        return overlay;
    }
}
