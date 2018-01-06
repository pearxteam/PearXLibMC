package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.lib.Color;
import ru.pearx.lib.Colors;
import ru.pearx.libmc.client.gui.DrawingTools;

/**
 * Created by mrAppleXZ on 02.05.17 8:53.
 */
@SideOnly(Side.CLIENT)
public class Button extends AbstractOfParts
{
    private String text;
    private Runnable clickAction;
    private Color textColor = Colors.WHITE;

    public Button(ResourceLocation textures, int textureSize, String str, Runnable run)
    {
        super(textures, textureSize);
        setText(str);
        setClickAction(run);
    }

    public Button(ResourceLocation textures, String str, Runnable run)
    {
        this(textures, 8, str, run);
    }

    @Override
    public void mouseUp(int button, int x, int y)
    {
        clickAction.run();
    }

    @Override
    public void render()
    {
        if(isFocused())
            GlStateManager.color(.8f, .8f, .8f, 1);
        super.render();
        GlStateManager.color(1, 1, 1, 1);
        String text = getText();
        DrawingTools.drawString(text, (getWidth() - DrawingTools.measureString(text)) / 2, (getHeight() - DrawingTools.getStringHeight(text)) / 2, getTextColor());
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Color getTextColor()
    {
        return textColor;
    }

    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
    }

    public Runnable getClickAction()
    {
        return clickAction;
    }

    public void setClickAction(Runnable clickAction)
    {
        this.clickAction = clickAction;
    }
}
