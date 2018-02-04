package ru.pearx.libmc.client.gui.controls;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mrAppleXZ on 16.04.17 13:12.
 */
@SideOnly(Side.CLIENT)
public class ControlList implements Collection<Control>
{
    public Control parent;
    private Collection<Control> lst = new ConcurrentLinkedQueue<>();

    public ControlList(Control parent)
    {
        this.parent = parent;
    }

    @Override
    public int size()
    {
        return lst.size();
    }

    @Override
    public boolean isEmpty()
    {
        return lst.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return lst.contains(o);
    }

    @Override
    public Iterator<Control> iterator()
    {
        return lst.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return lst.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts)
    {
        return lst.toArray(ts);
    }

    @Override
    public boolean add(Control control)
    {
        boolean bool = lst.add(control);
        if(bool)
        {
            control.setParent(parent);
            control.invokeInit();
        }
        return bool;
    }

    @Override
    public boolean remove(Object o)
    {
        boolean bool = lst.remove(o);
        if(bool && o instanceof Control)
        {
            ((Control) o).invokeClose();
            ((Control) o).setParent(null);
        }
        return bool;
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return lst.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Control> collection)
    {
        boolean bool = lst.addAll(collection);
        if(bool)
        {
            for (Control c : collection)
            {
                c.setParent(parent);
                c.invokeInit();
            }
        }
        return bool;
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        boolean bool = lst.removeAll(collection);
        if(bool)
        {
            for (Object o : collection)
            {
                if (o instanceof Control)
                {
                    ((Control) o).invokeClose();
                    ((Control) o).setParent(null);
                }
            }
        }
        return bool;
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        for (Control c : this)
        {
            if (!collection.contains(c))
            {
                c.invokeClose();
                c.setParent(null);
            }
        }
        return lst.retainAll(collection);
    }

    @Override
    public void clear()
    {
        for (Control c : this)
        {
            c.invokeClose();
            c.setParent(null);
        }
        lst.clear();
    }
}
