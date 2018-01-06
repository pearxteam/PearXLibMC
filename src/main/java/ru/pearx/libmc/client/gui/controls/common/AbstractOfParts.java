package ru.pearx.libmc.client.gui.controls.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.libmc.client.gui.TexturePart;
import ru.pearx.libmc.client.gui.controls.Control;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by mrAppleXZ on 05.01.18 17:07.
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractOfParts extends Control
{
    protected ResourceLocation textures;
    /* lu, u, ru,
       l, c, r,
       lb, b, rb */
    protected List<TexturePart> parts;
    protected int size;

    public AbstractOfParts(ResourceLocation textures, int size)
    {
        this.textures = textures;
        this.parts = new ArrayList<>();
        this.size = size;
        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 3; x++)
            {
                parts.add(new TexturePart(textures, x * size, y * size, size, size, size * 3, size * 3));
            }
        }
    }

    @Override
    public void render()
    {
        int w = getWidth();
        int h = getHeight();
        //upper and bottom
        for(int i = 1; i < (w - size) / (float)size; i++)
        {
            int x = i * size;
            parts.get(1).draw(x, 0);
            parts.get(7).draw(x, h - size);
        }
        //right and left
        for(int i = 1; i < (h - size) / (float)size; i++)
        {
            int y = i * size;
            parts.get(3).draw(0, y);
            parts.get(5).draw(w - size, y);
        }
        for (int xm = 1; xm < (w - size) / (float)size; xm++)
        {
            for(int ym = 1; ym < (h - size) / (float)size; ym++)
            {
                parts.get(4).draw(xm * size, ym * size);
            }
        }
        //corners
        parts.get(0).draw(0, 0);
        parts.get(2).draw(w - size, 0);
        parts.get(6).draw(0, h - size);
        parts.get(8).draw(w - size, h - size);
    }
}
