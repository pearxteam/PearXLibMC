package ru.pearx.libmc.client.debug;

import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/*
 * Created by mrAppleXZ on 19.08.17 8:42.
 */
@SideOnly(Side.CLIENT)
public class ModelRotationDebugger
{
    private int x, y, z;

    public ModelRotationDebugger()
    {

    }

    public ModelRotationDebugger(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quat4f getRotation()
    {
        boolean plus = Keyboard.isKeyDown(Keyboard.KEY_X);
        boolean min = Keyboard.isKeyDown(Keyboard.KEY_Z);
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
        {
            if(plus && y < 360)
            {
                y++;
            }
            if(min && y > -360)
            {
                y--;
            }
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_LMENU))
        {
            if(plus && z < 360)
            {
                z++;
            }
            if(min && z > -360)
            {
                z--;
            }
        }
        else
        {
            if(plus && x < 360)
            {
                x++;
            }
            if(min && x > -360)
            {
                x--;
            }
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_C))
        {
            x = 0;
            y = 0;
            z = 0;
        }
        return TRSRTransformation.quatFromXYZDegrees(new Vector3f(x, y, z));
    }
}
