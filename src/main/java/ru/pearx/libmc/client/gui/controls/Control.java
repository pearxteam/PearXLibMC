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
    private boolean selected;
    private boolean focused;

    public boolean initialized;
    public boolean keyEventsRS = true;
    public SelectType selectType = SelectType.CLICK;

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

    public boolean isSelected()
    {
        return selected;
    }

    public boolean isFocused()
    {
        return focused;
    }

    public void setFocused(boolean val)
    {
        focused = val;
    }


    public void selectionChanged()
    {

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

    public void invokeSelectionChanged()
    {
        selectionChanged();
    }

    public void invokeRender()
    {
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
        for (Control cont : controls)
            cont.invokeKeyDown(keycode);
        if (!keyEventsRS || isSelected())
            keyDown(keycode);
    }

    public void invokeKeyUp(int keycode)
    {
        for (Control cont : controls)
            cont.invokeKeyUp(keycode);
        if (!keyEventsRS || isSelected())
            keyUp(keycode);
    }

    public void invokeKeyPress(char key, int keycode)
    {
        for (Control cont : controls)
            cont.invokeKeyPress(key, keycode);
        if (!keyEventsRS || isSelected())
            keyPress(key, keycode);
    }

    public void invokeMouseDown(int button, int x, int y)
    {
        for (Control cont : controls)
        {
            if (new Rectangle(cont.getX(), cont.getY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                cont.invokeMouseDown(button, x - cont.getX(), y - cont.getY());
            }
        }
        if(selectType == SelectType.CLICK)
            select();
        mouseDown(button, x, y);
    }

    public void invokeMouseUp(int button, int x, int y)
    {
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
            setSelected(getMainParent(this), this);

    }

    public void invokeMouseEnter()
    {
        mouseEnter();
    }

    public void invokeMouseLeave()
    {
        mouseLeave();
    }

    public void invokeMouseWheel(int delta)
    {
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

    public void select()
    {
        unselectAll(getMainParent(this));
        selected = true;
        invokeSelectionChanged();
    }

    public void unselect()
    {
        selected = false;
        invokeSelectionChanged();
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

    public static void unselectAll(Control c)
    {
        c.unselect();
        for (Control cont : c.controls)
        {
            unselectAll(cont);
        }
    }

    public static void setSelected(Control c, Control select)
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
            setSelected(cont, select);
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