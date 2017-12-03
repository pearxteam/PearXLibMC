package ru.pearx.libmc.client.particle;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by mrAppleXZ on 27.08.17 14:39.
 */
@SideOnly(Side.CLIENT)
public abstract class PXParticle
{
    private double x, y, z;
    private int age;
    private int maxAge = 60;

    public PXParticle()
    {

    }

    public PXParticle(double x, double y, double z)
    {
        setX(x);
        setY(y);
        setZ(z);
    }

    public abstract float getWidth();
    public abstract float getHeight();
    public abstract void onRender();

    public void onUpdate()
    {
        setAge(getAge() + 1);
    }

    public boolean isExpired()
    {
        return getAge() >= getMaxAge();
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    //that's another magic number, yea
    public float getScaleFactor()
    {
        return 0.01875f;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    public void move(double px, double py, double pz)
    {
        setX(getX() + px);
        setY(getY() + py);
        setZ(getZ() + pz);
    }
}
