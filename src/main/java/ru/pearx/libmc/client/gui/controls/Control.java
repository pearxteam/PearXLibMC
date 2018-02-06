package ru.pearx.libmc.client.gui.controls;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.IGuiScreen;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by mrAppleXZ on 16.04.17 13:12.
 */
@SideOnly(Side.CLIENT)
public class Control
{
    private ControlCollection controls = new ControlCollection(this);
    private Control parent;
    @Nullable
    private IGuiScreen guiScreen = null;
    private Control mainParent = this;

    private int width;
    private int height;
    private int x;
    private int y;
    private boolean visible = true;
    private boolean focused;
    private boolean selected;
    private int lastMouseX;
    private int lastMouseY;
    private boolean initialized;

    //PROPERTIES

    public ControlCollection getControls()
    {
        return controls;
    }

    public Control getParent()
    {
        return parent;
    }

    public void setParent(Control parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            //update main parent if changed
            Control mp = getMainParentExplicit();
            Control lastMp = getMainParent();
            if(lastMp != mp)
                setMainParent(mp);
            //update gui screen if changed
            IGuiScreen gs = null;
            IGuiScreen lastGs = getGuiScreen();
            if (mp instanceof IGuiScreenProvider)
            {
                IGuiScreenProvider cont = (IGuiScreenProvider) mp;
                gs = cont.getGs();
            }
            if(gs != lastGs)
                setGuiScreen(gs);
        }
        else
        {
            setGuiScreen(null);
            setMainParent(null);
        }
    }

    private Control getMainParentExplicit()
    {
        Control c = this;
        while(c.getParent() != null)
        {
            c = c.getParent();
        }
        return c;
    }

    public Control getMainParent()
    {
        return mainParent;
    }

    private void setMainParent(Control mainParent)
    {
        this.mainParent = mainParent;
        for(Control child : getControls())
            child.setMainParent(mainParent);
    }

    @Nullable
    public IGuiScreen getGuiScreen()
    {
        return guiScreen;
    }

    private void setGuiScreen(IGuiScreen gs)
    {
        this.guiScreen = gs;
        for(Control child : getControls())
            child.setGuiScreen(gs);
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width, boolean triggerMove)
    {
        int prev = this.width;
        this.width = width;
        if(getParent() != null)
            getParent().invokeChildWidthChanged(this, prev, width);
        if(triggerMove)
            triggerMove();
    }

    public void setWidth(int width)
    {
        setWidth(width, true);
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height, boolean triggerMove)
    {
        int prev = this.height;
        this.height = height;
        if(getParent() != null)
            getParent().invokeChildHeightChanged(this, prev, height);
        if(triggerMove)
            triggerMove();
    }

    public void setHeight(int height)
    {
        setHeight(height, true);
    }

    public void triggerMove()
    {
        if(initialized)
        {
            if (getGuiScreen() != null && getMainParent() != null)
            {
                Control main = getMainParent();
                main.invokeMouseMove(getGuiScreen().getMouseX(), getGuiScreen().getMouseY(), 0, 0);
            }
        }
    }

    public int getX()
    {
        return x;
    }

    public int getTransformedX()
    {
        return getX() + (getParent() == null ? 0 : getParent().getOffsetX()) + getOwnOffsetX();
    }

    public void setX(int x, boolean triggerMove)
    {
        int prev = this.x;
        this.x = x;
        if(getParent() != null)
            getParent().invokeChildXChanged(this, prev, x);
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

    public int getTransformedY()
    {
        return getY() + (getParent() == null ? 0 : getParent().getOffsetY()) + getOwnOffsetY();
    }

    public void setY(int y, boolean triggerMove)
    {
        int prev = this.y;
        this.y = y;
        if(getParent() != null)
            getParent().invokeChildYChanged(this, prev, y);
        if(triggerMove)
            triggerMove();
    }

    public void setY(int y)
    {
        setY(y, true);
    }

    public void setPos(int x, int y, boolean triggerMove)
    {
        int prevX = this.x;
        int prevY = this.y;
        this.x = x;
        this.y = y;
        if (getParent() != null)
        {
            getParent().invokeChildXChanged(this, prevX, x);
            getParent().invokeChildYChanged(this, prevY, y);
        }
        if(triggerMove)
            triggerMove();
    }

    public void setPos(int x, int y)
    {
        setPos(x, y, true);
    }

    public void setSize(int w, int h, boolean triggerMove)
    {
        int prevW = this.width;
        int prevH = this.height;
        this.width = w;
        this.height = h;
        if (getParent() != null)
        {
            getParent().invokeChildWidthChanged(this, prevW, w);
            getParent().invokeChildHeightChanged(this, prevH, h);
        }
        if(triggerMove)
            triggerMove();
    }

    public void setSize(int w, int h)
    {
        setSize(w, h, true);
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

    private void setFocused(boolean val)
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
        for(Control child : getControls())
        {
            child.select(toSelect);
        }
    }

    public int getLastMouseX()
    {
        return lastMouseX;
    }

    private void setLastMouseX(int lastMouseX)
    {
        this.lastMouseX = lastMouseX;
    }

    public int getLastMouseY()
    {
        return lastMouseY;
    }

    private void setLastMouseY(int lastMouseY)
    {
        this.lastMouseY = lastMouseY;
    }

    public int getOffsetX()
    {
        return 0;
    }

    public int getOffsetY()
    {
        return 0;
    }

    public int getOwnOffsetX()
    {
        return 0;
    }

    public int getOwnOffsetY()
    {
        return 0;
    }

    public boolean shouldStencil()
    {
        return false;
    }

    //EVENTS


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

    public void update()
    {

    }

    public void childAdd(Control c)
    {

    }

    public void childRemove(Control c)
    {

    }

    public void childClear(Collection<Control> c)
    {

    }

    public void childXChanged(Control c, int prevX, int newX)
    {

    }

    public void childYChanged(Control c, int prevY, int newY)
    {

    }

    public void childWidthChanged(Control c, int prevW, int newW)
    {

    }

    public void childHeightChanged(Control c, int prevH, int newH)
    {

    }

    //EVENT INVOKES


    public void invokeRender(int stencilLevel)
    {
        if (!initialized)
            return;
        if (isVisible())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(getTransformedX(), getTransformedY(), 0);
            boolean stenc = shouldStencil();
            if(stenc)
            {
                int flag = stencilLevel + 1;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_STENCIL_TEST);
                GL11.glStencilFunc(GL11.GL_EQUAL, flag - 1, 0xFF);
                GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_INCR);
                GL11.glStencilMask(0xFF);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                if(stencilLevel == 0)
                {
                    GL11.glClearStencil(0);
                    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
                }
                DrawingTools.drawRectangle(0, 0, getWidth(), getHeight());
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glStencilFunc(GL11.GL_EQUAL, flag, 0xFF);
                GL11.glStencilMask(0);
                GL11.glColorMask(true, true, true, true);
                GL11.glDepthMask(true);
            }
            render();
            for (Control cont : getControls())
            {
                //todo check bounds
                cont.invokeRender(stenc ? stencilLevel + 1 : stencilLevel);
            }
            postRender();
            if(stenc && stencilLevel == 0)
                GL11.glDisable(GL11.GL_STENCIL_TEST);
            GlStateManager.popMatrix();
        }
    }

    public void invokeRender2()
    {
        if (!initialized)
            return;
        if (isVisible())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(getTransformedX(), getTransformedY(), 0);
            render2();
            for (Control cont : getControls())
            {
                cont.invokeRender2();
            }
            postRender2();

            GlStateManager.popMatrix();
        }
    }

    public void invokeKeyDown(int keycode)
    {
        if(!initialized)
            return;
        for (Control cont : getControls())
            cont.invokeKeyDown(keycode);
        keyDown(keycode);
    }

    public void invokeKeyUp(int keycode)
    {
        if(!initialized)
            return;
        for (Control cont : getControls())
            cont.invokeKeyUp(keycode);
        keyUp(keycode);
    }

    public void invokeKeyPress(char key, int keycode)
    {
        if(!initialized)
            return;
        for (Control cont : getControls())
            cont.invokeKeyPress(key, keycode);
        keyPress(key, keycode);
    }

    public void invokeMouseDown(int button, int x, int y)
    {
        boolean last = true;
        if (!initialized)
            return;
        for (Control cont : getControls())
        {
            if (new Rectangle(cont.getTransformedX(), cont.getTransformedY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                last = false;
                cont.invokeMouseDown(button, x - cont.getTransformedX(), y - cont.getTransformedY());
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
        for (Control cont : getControls())
        {
            if (new Rectangle(cont.getTransformedX(), cont.getTransformedY(), cont.getWidth(), cont.getHeight()).contains(x, y))
            {
                last = false;
                cont.invokeMouseUp(button, x - cont.getTransformedX(), y - cont.getTransformedY());
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
        for (Control cont : getControls())
        {
            if ((cont.getTransformedX() <= x && cont.getTransformedX() + cont.getWidth() >= x) && (cont.getTransformedY() <= y && cont.getTransformedY() + cont.getHeight() >= y))
            {
                cont.invokeMouseMove(x - cont.getTransformedX(), y - cont.getTransformedY(), dx, dy);
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
        for (Control cont : getControls())
        {
            cont.invokeMouseWheel(delta);
        }
        mouseWheel(delta);
    }

    public void invokeInit()
    {
        if (!initialized)
        {
            initialized = true;
            init();
        }
        triggerMove();
    }

    public void invokeChildAdd(Control c)
    {
        if(!initialized)
            return;
        childAdd(c);
    }

    public void invokeChildRemove(Control c)
    {
        if(!initialized)
            return;
        childRemove(c);
    }

    public void invokeChildClear(Collection<Control> c)
    {
        if(!initialized)
            return;
        childClear(c);
    }

    public void invokeChildXChanged(Control c, int prevX, int newX)
    {
        if(!initialized)
            return;
        childXChanged(c, prevX, newX);
    }

    public void invokeChildYChanged(Control c, int prevY, int newY)
    {
        if(!initialized)
            return;
        childYChanged(c, prevY, newY);
    }

    public void invokeChildWidthChanged(Control c, int prevW, int newW)
    {
        if(!initialized)
            return;
        childWidthChanged(c, prevW, newW);
    }

    public void invokeChildHeightChanged(Control c, int prevH, int newH)
    {
        if(!initialized)
            return;
        childHeightChanged(c, prevH, newH);
    }

    public void invokeUpdate()
    {
        if(!initialized)
            return;
        update();
        for(Control c : getControls())
            c.invokeUpdate();
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
        for(Control cont : getControls())
            cont.invokeClose();
        close();
    }

    public Point getPosOnScreen()
    {
        int x = getTransformedX();
        int y = getTransformedY();
        Control parent = getParent();
        while(parent != null)
        {
            x += parent.getTransformedY();
            y += parent.getTransformedY();
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
        for (Control cont : c.getControls())
            setFocused(cont, select);
    }
}
