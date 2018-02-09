package ru.pearx.libmc.client.gui.controls;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.pearx.lib.collections.event.CollectionEventHandler;
import ru.pearx.lib.collections.event.EventCollection;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mrAppleXZ on 16.04.17 13:12.
 */
@SideOnly(Side.CLIENT)
public class ControlCollection extends EventCollection<Control>
{
    public Control parent;

    public ControlCollection(Control parent)
    {
        super(new ConcurrentLinkedQueue<>(), new CollectionEventHandler<Control>()
        {
            @Override
            public void onAdd(Control control)
            {
                setParentAndInit(control);
                parent.invokeChildAdd(control);
            }

            @Override
            public void onAdd(Collection<? extends Control> t)
            {
                for(Control c : t)
                    onAdd(c);
            }

            @Override
            public void onRemove(Object o)
            {
                if(o instanceof Control)
                {
                    Control c = (Control)o;
                    removeParentAndClose(c);
                    parent.invokeChildRemove(c);
                }
            }

            @Override
            public void onRemove(Collection<Object> col)
            {
                for(Object o : col)
                    onRemove(o);
            }

            @Override
            public void onClear(Collection<Control> t)
            {
                for(Control c : t)
                    removeParentAndClose(c);
                parent.invokeChildClear(t);
            }

            private void setParentAndInit(Control c)
            {
                c.setParent(parent);
                c.invokeInit();
            }

            private void removeParentAndClose(Control c)
            {
                c.invokeClose();
                c.setParent(null);
            }
        });
        this.parent = parent;
    }
}
