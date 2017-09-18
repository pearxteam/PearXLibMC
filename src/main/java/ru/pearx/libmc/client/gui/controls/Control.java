package ru.pearx.libmc.client.gui.controls;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import ru.pearx.libmc.client.gui.IGuiScreen;

/**
 * Created by mrAppleXZ on 16.04.17 13:12.
 */
@SideOnly(Side.CLIENT)
public class Control
{
    public ControlList controls = new ControlList(this);
    private Control parent;

    private int width;
    private int height;
    private int x;
    private int y;
    private boolean visible = true;
    private boolean focused;
    private boolean canBeSelected;
    private boolean selected;

    public boolean initialized;

    public Control getParent()
    {
        return parent;
    }

    public void setParent(Control parent)
    {
        this.parent = parent;
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

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setPos(int x, int y)
    {
        setX(x);
        setY(y);
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

    public boolean canBeSelected()
    {
        return canBeSelected;
    }

    public void setCanBeSelected(boolean canBeSelected)
    {
        this.canBeSelected = canBeSelected;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public void render()
    {

    }

    public void postRender()
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
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 700);
        if(isVisible())
            postRender();
        GlStateManager.popMatrix();
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
        if(!initialized)
            return;
        boolean last = true;
        for (Control cont : controls)
        {
            if (new Rectangle(cont.getX(), cont.getY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                cont.invokeMouseDown(button, x - cont.getX(), y - cont.getY());
                if(canBeSelected())
                {
                    setSelected(false);
                }
                last = false;
            }
        }
        if(last && canBeSelected())
            setSelected(true);
        mouseDown(button, x, y);
    }

    public void invokeMouseUp(int button, int x, int y)
    {
        if(!initialized)
            return;
        for (Control cont : controls)
        {
            if (new Rectangle(cont.getX(), cont.getY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                cont.invokeMouseUp(button, x - cont.getX(), y - cont.getY());
            }
        }
        mouseUp(button, x, y);
    }

    public void invokeMouseMove(int x, int y, int dx, int dy)
    {
        if(!initialized)
            return;
        boolean last = true;
        for (Control cont : controls)
        {
            if (new Rectangle(cont.getX(), cont.getY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                cont.invokeMouseMove(x - cont.getX(), y - cont.getY(), dx, dy);
                last = false;
            }
        }
        mouseMove(x, y, dx, dy);
        if(last)
            setFocused(getMainParent(this), this);

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
        Control parent = getMainParent(this);
        if (parent.getGuiScreen() != null)
            parent.invokeMouseMove(parent.getGuiScreen().getMouseX(), parent.getGuiScreen().getMouseY(), 0, 0);
    }

    public IGuiScreen getGuiScreen()
    {
        Control parent = getMainParent(this);
        if(parent instanceof GuiControlContainer)
        {
            GuiControlContainer cont = (GuiControlContainer) parent;
            return cont.getGs();
        }
        return null;
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

    public static Control getMainParent(Control c)
    {
        if (c.getParent() != null)
            return getMainParent(c.getParent());
        return c;
    }

    public Point getPointOnGui()
    {
        return getPointOnGui(this, new Point());
    }

    private static Point getPointOnGui(Control c, Point now)
    {
        if (c != null)
        {
            return getPointOnGui(c.getParent(), new Point(c.getX() + now.getX(), c.getY() + now.getY()));
        }
        return now;
    }
}
