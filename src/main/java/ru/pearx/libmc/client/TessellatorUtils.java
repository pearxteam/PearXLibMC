package ru.pearx.libmc.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.ByteOrder;
import java.nio.IntBuffer;

/*
 * Created by mrAppleXZ on 23.07.17 14:04.
 */
@SideOnly(Side.CLIENT)
public class TessellatorUtils
{
    public static void multiplyColor(BufferBuilder buffer, int vertexIndex, float r, float g, float b, float a)
    {
        int index = buffer.getColorIndex(vertexIndex);
        int col = -1;

        if (!buffer.isColorDisabled())
        {
            col = buffer.rawIntBuffer.get(index);

            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            {
                int red = (int)((col & 255) * r);
                int green = (int)((col >> 8 & 255) * g);
                int blue = (int)((col >> 16 & 255) * b);
                int alpha = (int)((col >> 24 & 255) * a);
                col = alpha << 24 | blue << 16 | green << 8 | red;
            }
            else
            {
                int alpha = (int)((col & 255) * a);
                int blue = (int)((col >> 8 & 255) * b);
                int green = (int)((col >> 16 & 255) * g);
                int red = (int)((col >> 24 & 255) * r);
                col = red << 24 | green << 16 | blue << 8 | alpha;
            }
        }

        buffer.rawIntBuffer.put(index, col);
    }
}
