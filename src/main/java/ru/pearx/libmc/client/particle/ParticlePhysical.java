package ru.pearx.libmc.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/*
 * Created by mrAppleXZ on 03.12.17 10:26.
 */
public abstract class ParticlePhysical extends PXParticle
{
    private AxisAlignedBB boundingBox;
    private float dx, dy, dz;
    private double lastMoveX, lastMoveY, lastMoveZ;

    public ParticlePhysical(double x, double y, double z)
    {
        super(x, y, z);
        float bb = getWidth() * getScaleFactor() / 2f;
        setBoundingBox(new AxisAlignedBB(-bb, -bb, -bb, bb, bb, bb));
    }

    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public float getDx()
    {
        return dx;
    }

    public void setDx(float dx)
    {
        this.dx = dx;
    }

    public float getDy()
    {
        return dy;
    }

    public void setDy(float dy)
    {
        this.dy = dy;
    }

    public float getDz()
    {
        return dz;
    }

    public void setDz(float dz)
    {
        this.dz = dz;
    }

    public double getLastMoveX()
    {
        return lastMoveX;
    }

    public double getLastMoveY()
    {
        return lastMoveY;
    }

    public double getLastMoveZ()
    {
        return lastMoveZ;
    }

    @Override
    public void move(double px, double py, double pz)
    {
        //todo change aabbs to mutable variants?
        AxisAlignedBB aabb = getBoundingBox().offset(getX(), getY(), getZ());

        List<AxisAlignedBB> boxes = Minecraft.getMinecraft().world.getCollisionBoxes(null, aabb.expand(px, py, pz));
        if(px != 0)
        {
            for (AxisAlignedBB b : boxes)
            {
                px = b.calculateXOffset(aabb, px);
            }
            aabb = aabb.offset(px, 0, 0);
        }
        if(py != 0)
        {
            for (AxisAlignedBB b : boxes)
            {
                py = b.calculateYOffset(aabb, py);
            }
            aabb = aabb.offset(0, py, 0);
        }
        if(pz != 0)
        {
            for (AxisAlignedBB b : boxes)
            {
                pz = b.calculateZOffset(aabb, pz);
            }
            aabb = aabb.offset(0, 0, pz);
        }
        lastMoveX = px;
        lastMoveY = py;
        lastMoveZ = pz;
        super.move(px, py, pz);
    }
}
