package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.lib.Color;
import ru.pearx.lib.Colors;
import ru.pearx.lib.collections.EventList;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by mrAppleXZ on 27.09.17 16:59.
 */
@SideOnly(Side.CLIENT)
public class ContextMenu extends Control
{
    public static class Element
    {
        private String name;
        private Runnable runnable;

        public Element()
        {
        }

        public Element(String name, Runnable runnable)
        {
            this.name = name;
            this.runnable = runnable;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Runnable getRunnable()
        {
            return runnable;
        }

        public void setRunnable(Runnable runnable)
        {
            this.runnable = runnable;
        }
    }

    private EventList<Element> elements = new EventList<>(new ArrayList<>(), this::updateMeasurements);
    private int margin = 3;
    private Color textColor = Colors.WHITE;
    private Color elementMouseOverColor = Color.fromRGB(25, 25, 25);

    public class Button extends Control
    {
        private Element element;

        public Button(int x, int y, Element element)
        {
            setPos(x, y);
            setWidth(ContextMenu.this.getWidth());
            setHeight(DrawingTools.getFontHeight());

            this.element = element;
        }

        @Override
        public void render()
        {
            if(isFocused())
                DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), getElementMouseOverColor(), getElementMouseOverColor());
            DrawingTools.drawString(element.getName(), margin, 0, getTextColor());
        }

        @Override
        public void mouseUp(int button, int x, int y)
        {
            element.runnable.run();
            ContextMenu.this.getParent().controls.remove(ContextMenu.this);
        }
    }

    public ContextMenu(Element... elems)
    {
        elements.addAll(Arrays.asList(elems));
        updateMeasurements();
    }

    public List<Element> getElements()
    {
        return elements;
    }

    public Color getTextColor()
    {
        return textColor;
    }

    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
    }

    public Color getElementMouseOverColor()
    {
        return elementMouseOverColor;
    }

    public void setElementMouseOverColor(Color elementMouseOverColor)
    {
        this.elementMouseOverColor = elementMouseOverColor;
    }

    private void updateMeasurements()
    {
        controls.removeIf((Control cont) -> cont instanceof Button);
        int maxW = 0;
        int y = margin;
        for(Element e : getElements())
        {
            int w = DrawingTools.measureString(e.getName());
            if (w > maxW)
                maxW = w;
        }
        setWidth(margin + maxW + margin);
        setHeight(margin + (DrawingTools.getFontHeight() + margin) * getElements().size());
        for(Element e : getElements())
        {
            Button butt = new Button(0, y, e);
            controls.add(butt);
            y += butt.getHeight() + margin;
        }
    }

    @Override
    public void render()
    {
        DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), Colors.GREY_800, Colors.GREY_800);
    }
}
