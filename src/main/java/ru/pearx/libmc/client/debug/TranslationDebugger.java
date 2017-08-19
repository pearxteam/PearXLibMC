package ru.pearx.libmc.client.debug;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3f;

/*
 * Created by mrAppleXZ on 19.08.17 8:40.
 */
@SideOnly(Side.CLIENT)
public class TranslationDebugger
{
    private float x, y, z;

    public TranslationDebugger()
    {

    }

    public TranslationDebugger(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f getTranslation()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_Z))
        {
            x-= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_X))
        {
            x+= 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_C))
        {
            y-= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_V))
        {
            y+= 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_B))
        {
            z-= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_N))
        {
            z+= 0.01f;
        }
        return new Vector3f(x, y, z);
    }
}
