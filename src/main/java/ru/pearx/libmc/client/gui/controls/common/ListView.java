package ru.pearx.libmc.client.gui.controls.common;

import ru.pearx.lib.Color;
import ru.pearx.lib.Colors;
import ru.pearx.lib.collections.EventList;
import ru.pearx.lib.math.MathUtils;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * Created by mrAppleXZ on 27.09.17 19:19.
 */
public class ListView extends Control
{
    private Color backgroundColor = Colors.GREY_800;
    private Color textColor = Colors.WHITE;
    private Color selectedColor = Colors.BLUE_500;
    private EventList<String> elements = new EventList<>(new LinkedList<>(), () ->
    {
        if(getElements().size() <= getSelection())
        {
            setSelection(getElements().size() - 1);
        }
    });
    private int selection = -1;

    public ListView(String... elements)
    {
        this.elements.addAll(Arrays.asList(elements));
    }

    public List<String> getElements()
    {
        return elements;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public int getSelection()
    {
        return selection;
    }

    public void setSelection(int selection)
    {
        int i = this.selection;
        this.selection = selection;
        onSelectionChanged(i, selection);
    }

    public Color getTextColor()
    {
        return textColor;
    }

    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
    }

    @Override
    public void render()
    {
        DrawingTools.drawGradientRect(0, 0, getWidth(), getHeight(), getBackgroundColor());
        int y = 0;
        for(int i = 0; i < getElements().size(); i++)
        {
            String elem = getElements().get(i);
            if(getSelection() == i)
                DrawingTools.drawGradientRect(0, y, getWidth(), DrawingTools.getFontHeight(), selectedColor);
            int w = 0;
            StringBuilder sb = new StringBuilder();
            for (char ch : elem.toCharArray())
            {
                w += DrawingTools.measureChar(ch);
                sb.append(ch);
                if(w > getWidth())
                    break;
            }
            DrawingTools.drawString(sb.toString(), 0, y, getTextColor());
            y += DrawingTools.getFontHeight();
        }
    }

    @Override
    public void mouseUp(int button, int x, int y)
    {
        int[] arr = new int[getElements().size()];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = DrawingTools.getFontHeight() * i + DrawingTools.getFontHeight() / 2;
        }
        setSelection(arr.length == 0 ? -1 : MathUtils.getNearest(arr, y));
    }

    public void onSelectionChanged(int old, int nw)
    {

    }
}
