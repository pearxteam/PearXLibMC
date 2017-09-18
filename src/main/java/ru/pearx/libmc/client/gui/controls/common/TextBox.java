package ru.pearx.libmc.client.gui.controls.common;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.pearx.lib.Color;
import ru.pearx.lib.Colors;
import ru.pearx.lib.math.MathUtils;
import ru.pearx.libmc.client.gui.DrawingTools;
import ru.pearx.libmc.client.gui.controls.Control;

/*
 * Created by mrAppleXZ on 17.09.17 20:31.
 */
public class TextBox extends Control
{
    private ResourceLocation texture;
    private Color textColor = Colors.WHITE;
    private StringBuilder buffer = new StringBuilder();
    private int focus = 0;
    private int selectionStart = 0;
    private int renderFrom = 0;
    private int maxRenderLength = 16;
    private int maxLength = Integer.MAX_VALUE;

    public TextBox(ResourceLocation texture)
    {
        setTexture(texture);
        setCanBeSelected(true);
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    public void setTexture(ResourceLocation texture)
    {
        this.texture = texture;
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
    public int getHeight()
    {
        return 9 + 4;
    }

    private int getRenderTo()
    {
        int l = renderFrom + getMaxRenderLength();
        if (buffer.length() < l)
            l = buffer.length();
        return l;
    }

    private int getClicked(int x)
    {
        if (buffer.length() > 0)
        {
            char[] cut = buffer.substring(renderFrom, getRenderTo()).toCharArray();
            int[] ints = new int[cut.length + 1];
            ints[0] = 0;
            for (int i = 0; i < cut.length; i++)
            {
                boolean u = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
                Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
                ints[i + 1] = (i == 0 ? 2 : ints[i]) + DrawingTools.measureChar(cut[i]);
                Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(u);
            }
            return MathUtils.getNearest(ints, x);
        }
        return 0;
    }

    private void removeSelection()
    {
        if(selectionStart != focus)
        {
            int start = Math.min(selectionStart, focus);
            int stop = Math.max(selectionStart, focus);
            buffer.delete(start, stop);
            changeFocusAndSelection(-(focus - selectionStart));
        }
    }

    public StringBuilder getBuffer()
    {
        return buffer;
    }

    public int getMaxRenderLength()
    {
        return maxRenderLength;
    }

    public void setMaxRenderLength(int maxRenderLength)
    {
        this.maxRenderLength = maxRenderLength;
    }

    public int getFocus()
    {
        return focus;
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength(int maxLength)
    {
        this.maxLength = maxLength;
    }

    @Override
    public void render()
    {
        for(int x = 8; x < getWidth() - 8; x += 8)
        {
            DrawingTools.drawTexture(getTexture(), x, 0, 8, getHeight(), 8, 0, 24, getHeight());
        }
        DrawingTools.drawTexture(getTexture(), 0, 0, 8, getHeight(), 0, 0, 24, getHeight());
        DrawingTools.drawTexture(getTexture(), getWidth() - 8, 0, 8, getHeight(), 16, 0, 24, getHeight());

        StringBuilder cut = new StringBuilder(buffer.substring(renderFrom, getRenderTo()));
        if(selectionStart != focus)
        {
            int start = Math.min(selectionStart, focus) - renderFrom;
            int stop = Math.max(selectionStart, focus) - renderFrom;
            if(start < 0)
                start = 0;
            if(stop > getMaxRenderLength())
                stop = getMaxRenderLength();
            cut.insert(start, "§9§n");
            cut.insert(stop + 4, "§r");
        }
        boolean u = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
        Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
        DrawingTools.drawString(cut.toString(), 4, 2, getTextColor());

        int x = DrawingTools.measureString(buffer.substring(renderFrom, focus)) + 4;
        Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(u);
        if(isSelected() && System.currentTimeMillis() / 300 % 2 == 0)
            DrawingTools.drawLine(x, 2, x, getHeight() - 2, 2, Colors.BLACK, Colors.BLACK);
    }

    @Override
    public void keyDown(int keycode)
    {
        if (isSelected())
        {
            if (keycode == Keyboard.KEY_BACK)
            {
                if (selectionStart != focus)
                {
                    removeSelection();
                } else if (focus <= buffer.length() && focus > 0)
                {
                    buffer.deleteCharAt(focus - 1);
                    changeFocusAndSelection(-1);
                }
            } else if (keycode == Keyboard.KEY_DELETE)
            {
                if (selectionStart != focus)
                {
                    removeSelection();
                } else if (focus < buffer.length())
                {
                    buffer.deleteCharAt(focus);
                }
            } else if (keycode == Keyboard.KEY_LEFT)
            {
                changeFocusAndSelection(-1);
            } else if (keycode == Keyboard.KEY_RIGHT)
            {
                changeFocusAndSelection(1);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
            {
                if (keycode == Keyboard.KEY_V)
                {
                    String s = GuiScreen.getClipboardString().replaceAll("\\p{Cntrl}", "");
                    insert(s);
                }
                if (keycode == Keyboard.KEY_A)
                {
                    setSelection(0);
                    changeFocus(buffer.length() - focus);
                }
                if (keycode == Keyboard.KEY_C)
                {
                    if (selectionStart != focus)
                    {
                        GuiScreen.setClipboardString(getSelectedText());
                    }
                }
                if(keycode == Keyboard.KEY_X)
                {
                    if (selectionStart != focus)
                    {
                        GuiScreen.setClipboardString(getSelectedText());
                        removeSelection();
                    }
                }
            }
        }
    }

    @Override
    public void keyPress(char key, int keycode)
    {
        if (isSelected())
        {
            if (Character.getType(key) != Character.CONTROL)
            {
                insert(key);
            }
        }
    }

    @Override
    public void mouseDown(int button, int x, int y)
    {
        changeFocusAndSelection(-((focus - renderFrom) - getClicked(x)));
    }

    public void changeFocus(int i)
    {
        if((focus + i) <= buffer.length() && (focus + i) >= 0)
        {
            focus += i;
            if((focus < (renderFrom + 1) && renderFrom > 0) || (focus > (getRenderTo() - 1) && getRenderTo() < buffer.length()))
            {
                if(renderFrom == 0 && i > getMaxRenderLength())
                    renderFrom -= getMaxRenderLength();
                renderFrom += i;
            }
            if(renderFrom < 0)
                renderFrom = 0;
        }
    }

    public void changeFocusAndSelection(int i)
    {
        changeFocus(i);
        setSelection(focus);
    }

    public void setSelection(int i)
    {
        selectionStart = i;
    }

    @Override
    public void mouseMove(int x, int y, int dx, int dy)
    {
        if(Mouse.isButtonDown(0))
        {
            changeFocus(-((focus - renderFrom) - getClicked(x)));
        }
    }

    public void insert(String s)
    {
        removeSelection();
        if(s.length() + buffer.length() > getMaxLength())
        {
            s = s.substring(0, getMaxLength() - buffer.length());
        }
        buffer.insert(focus, s);
        changeFocusAndSelection(s.length());
    }

    public void insert(char ch)
    {
        insert(Character.toString(ch));
    }

    public String getSelectedText()
    {
        if (selectionStart != focus)
        {
            int start = Math.min(selectionStart, focus);
            int stop = Math.max(selectionStart, focus);
            return buffer.substring(start, stop);
        }
        return "";
    }
}
