package ru.pearx.libmc.client.gui.controls;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import ru.pearx.libmc.client.gui.IGuiScreen;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by mrAppleXZ on 16.04.17 13:12.
 */
@SideOnly(Side.CLIENT)
public class Control
{
    public ControlList controls = new ControlList(this);
    private Control parent;
    @Nullable
    private IGuiScreen guiScreen = null;

    private int width;
    private int height;
    private int x;
    private int y;
    private boolean visible = true;
    private boolean focused;
    private boolean selected;
    private int lastMouseX;
    private int lastMouseY;

    public boolean initialized;

    public Control getParent()
    {
        return parent;
    }

    public void setParent(Control parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            Control mp = getMainParent();
            if (mp instanceof IGuiScreenProvider)
            {
                IGuiScreenProvider cont = (IGuiScreenProvider) mp;
                this.guiScreen = cont.getGs();
            }
        }
        else
            this.guiScreen = null;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void triggerMove()
    {
        if(initialized)
        {
            Control main = getMainParent();
            if (getGuiScreen() != null)
                main.invokeMouseMove(getGuiScreen().getMouseX(), getGuiScreen().getMouseY(), 0, 0);
        }
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x, boolean triggerMove)
    {
        this.x = x;
        if(triggerMove)
            triggerMove();
    }

    public void setX(int x)
    {
        setX(x, true);
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y, boolean triggerMove)
    {
        this.y = y;
        if(triggerMove)
            triggerMove();
    }

    public void setY(int y)
    {
        setY(y, true);
    }

    public void setPos(int x, int y, boolean triggerMove)
    {
        this.x = x;
        this.y = y;
        if(triggerMove)
            triggerMove();
    }

    public void setPos(int x, int y)
    {
        setPos(x, y, true);
    }

    public void setSize(int w, int h)
    {
        setWidth(w);
        setHeight(h);
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isFocused()
    {
        return focused;
    }

    public void setFocused(boolean val)
    {
        focused = val;
    }

    public boolean isSelected()
    {
        return selected;
    }

    private void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public void select()
    {
        getMainParent().select(this);
    }

    private void select(Control toSelect)
    {
        setSelected(this == toSelect);
        for(Control child : controls)
        {
            child.select(toSelect);
        }
    }

    public int getLastMouseX()
    {
        return lastMouseX;
    }

    public void setLastMouseX(int lastMouseX)
    {
        this.lastMouseX = lastMouseX;
    }

    public int getLastMouseY()
    {
        return lastMouseY;
    }

    public void setLastMouseY(int lastMouseY)
    {
        this.lastMouseY = lastMouseY;
    }

    public void render()
    {

    }

    public void render2()
    {

    }

    public void postRender()
    {

    }

    public void postRender2()
    {

    }

    public void keyDown(int keycode)
    {

    }

    public void keyUp(int keycode)
    {

    }

    public void keyPress(char key, int keycode)
    {

    }

    public void mouseDown(int button, int x, int y)
    {

    }

    public void mouseUp(int button, int x, int y)
    {

    }

    public void mouseMove(int x, int y, int dx, int dy)
    {

    }

    public void mouseEnter()
    {

    }

    public void mouseLeave()
    {

    }

    public void mouseWheel(int delta)
    {

    }

    public void init()
    {

    }

    public void close()
    {

    }



    public void invokeRender()
    {
        if(!initialized)
            return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(getX(), getY(), 0);
        if(isVisible())
            render();
        for(Control cont : controls)
        {
            cont.invokeRender();
        }
        if(isVisible())
            postRender();
        GlStateManager.popMatrix();
    }

    public void invokeRender2()
    {
        if(!initialized)
            return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(getX(), getY(), 0);
        if(isVisible())
            render2();
        for(Control cont : controls)
        {
            cont.invokeRender2();
        }
        if(isVisible())
            postRender2();
        GlStateManager.popMatrix();
    }

    public void invokeKeyDown(int keycode)
    {
        if(!initialized)
            return;
        for (Control cont : controls)
            cont.invokeKeyDown(keycode);
        keyDown(keycode);
    }

    public void invokeKeyUp(int keycode)
    {
        if(!initialized)
            return;
        for (Control cont : controls)
            cont.invokeKeyUp(keycode);
        keyUp(keycode);
    }

    public void invokeKeyPress(char key, int keycode)
    {
        if(!initialized)
            return;
        for (Control cont : controls)
            cont.invokeKeyPress(key, keycode);
        keyPress(key, keycode);
    }

    public void invokeMouseDown(int button, int x, int y)
    {
        boolean last = true;
        if (!initialized)
            return;
        for (Control cont : controls)
        {
            if (new Rectangle(cont.getX(), cont.getY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                last = false;
                cont.invokeMouseDown(button, x - cont.getX(), y - cont.getY());
            }
        }
        if(last)
        {
            select();
            mouseDown(button, x, y);
        }
    }

    public void invokeMouseUp(int button, int x, int y)
    {
        boolean last = true;
        if(!initialized)
            return;
        for (Control cont : controls)
        {
            if (new Rectangle(cont.getX(), cont.getY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                last = false;
                cont.invokeMouseUp(button, x - cont.getX(), y - cont.getY());
            }
        }
        if(last)
            mouseUp(button, x, y);
    }

    public void invokeMouseMove(int x, int y, int dx, int dy)
    {
        if(!initialized)
            return;
        boolean last = true;
        for (Control cont : controls)
        {
            if ((cont.getX() <= x && cont.getX() + cont.getWidth() >= x) && (cont.getY() <= y && cont.getY() + cont.getHeight() >= y))
            {
                cont.invokeMouseMove(x - cont.getX(), y - cont.getY(), dx, dy);
                last = false;
            }
        }
        mouseMove(x, y, dx, dy);
        setLastMouseX(x);
        setLastMouseY(y);
        if(last)
            setFocused(getMainParent(), this);
    }

    public void invokeMouseEnter()
    {
        if(!initialized)
            return;
        mouseEnter();
    }

    public void invokeMouseLeave()
    {
        if(!initialized)
            return;
        mouseLeave();
    }

    public void invokeMouseWheel(int delta)
    {
        if(!initialized)
            return;
        for (Control cont : controls)
        {
            cont.invokeMouseWheel(delta);
        }
        mouseWheel(delta);
    }

    public void invokeInit()
    {
        if (!initialized)
        {
            init();
            initialized = true;
        }
        Control parent = getMainParent();
        if (getGuiScreen() != null)
            parent.invokeMouseMove(getGuiScreen().getMouseX(), getGuiScreen().getMouseY(), 0, 0);
    }

    public GuiControlContainer.OverlayContainer getOverlay()
    {
        Control c = getMainParent();
        if(c instanceof IOverlayProvider)
            return c.getOverlay();
        return null;
    }

    public void invokeClose()
    {
        if(!initialized)
            return;
        for(Control cont : controls)
            cont.invokeClose();
        close();
    }

    @Nullable
    public IGuiScreen getGuiScreen()
    {
        return guiScreen;
    }

    public Control getMainParent()
    {
        Control c = this;
        while(c.getParent() != null)
        {
            c = c.getParent();
        }
        return c;
    }

    public Point getPosOnScreen()
    {
        int x = getX();
        int y = getY();
        Control parent = getParent();
        while(parent != null)
        {
            x += parent.getX();
            y += parent.getY();
            parent = parent.getParent();
        }
        return new Point(x, y);
    }

    public void drawHoveringText(String s, int x, int y)
    {
        GlStateManager.pushMatrix();
        Point pos = getPosOnScreen();
        GlStateManager.translate(-pos.getX(), -pos.getY(), 0);
        getGuiScreen().drawHovering(s, x + pos.getX(), y + pos.getY());
        GlStateManager.popMatrix();
    }

    public static void setFocused(Control c, Control select)
    {
        if(c.isFocused() && c != select)
        {
            c.setFocused(false);
            c.invokeMouseLeave();
        }
        if(!c.isFocused() && c == select)
        {
            c.setFocused(true);
            c.invokeMouseEnter();
        }
        for (Control cont : c.controls)
            setFocused(cont, select);
    }
}
