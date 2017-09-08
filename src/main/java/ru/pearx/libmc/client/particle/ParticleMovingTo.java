package ru.pearx.libmc.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3d;

/**
 * Created by mrAppleXZ on 28.05.17 18:45.
 */
@SideOnly(Side.CLIENT)
public abstract class ParticleMovingTo extends PXParticle
{
    protected Vector3d actualSpeed;
    protected float speed;

    protected ParticleMovingTo(Vector3d loc, Vector3d locTo, float speed)
    {
        super(loc.getX(), loc.getY(), loc.getZ());

        Vector3d vec = new Vector3d(locTo);
        vec.sub(loc);
        double length = vec.length();
        setMaxAge(MathHelper.ceil(length / speed));
        this.actualSpeed = new Vector3d((vec.getX() / length) * speed, (vec.getY() / length) * speed, (vec.getZ() / length) * speed);
        this.speed = speed;
    }

    @Override
    public void onUpdate()
    {
        move(actualSpeed.getX(), actualSpeed.getY(), actualSpeed.getZ());
        super.onUpdate();
    }
}
