package ru.pearx.libmc.client;

import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created by mrAppleXZ on 05.06.17 21:43.
 */
@SideOnly(Side.CLIENT)
/**
 * A simple tool for making your model's rotation directly in the game.
 */
public class ClientDebugger
{
    private static int rotX, rotY, rotZ;

    public static Quat4f getRotation()
    {
        boolean plus = Keyboard.isKeyDown(Keyboard.KEY_X);
        boolean min = Keyboard.isKeyDown(Keyboard.KEY_Z);
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
        {
            if(plus && rotY < 360)
            {
                rotY++;
            }
            if(min && rotY > -360)
            {
                rotY--;
            }
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_LMENU))
        {
            if(plus && rotZ < 360)
            {
                rotZ++;
            }
            if(min && rotZ > -360)
            {
                rotZ--;
            }
        }
        else
        {
            if(plus && rotX < 360)
            {
                rotX++;
            }
            if(min && rotX > -360)
            {
                rotX--;
            }
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_C))
        {
            rotX = 0;
            rotY = 0;
            rotZ = 0;
        }
        return TRSRTransformation.quatFromXYZDegrees(new Vector3f(rotX, rotY, rotZ));
    }

    private static float traX, traY, traZ;

    public static Vector3f getTranslation()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_Z))
        {
            traX-= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_X))
        {
            traX+= 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_C))
        {
            traY-= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_V))
        {
            traY+= 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_B))
        {
            traZ-= 0.01f;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_N))
        {
            traZ+= 0.01f;
        }
        return new Vector3f(traX, traY, traZ);
    }
}
