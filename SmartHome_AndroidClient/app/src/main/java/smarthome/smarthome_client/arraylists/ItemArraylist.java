package smarthome.smarthome_client.arraylists;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.Attributes;

import smarthome.smarthome_client.models.interfaces.NameableItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemArraylist<T extends NameableItem> extends ArrayList<T>
{
    public ItemArraylist()
    {
        super();
    }

    public ItemArraylist(ArrayList<T> initialList)
    {
        for (T item : initialList)
        {
            add(item);
        }
    }

    public T getItem(String name)
    {
        for (T item : this)
        {
            if (item.getName().equals(name))
            {
                return item;
            }
        }
        return null;
    }

    public int getPosition(String name)
    {
        for (int i = 0; i < size(); i++)
        {
            if (get(i).getName().equals(name))
            {
                return i;
            }
        }
        return -1;
    }

    public int getPosition(T item)
    {
        return getPosition(item.getName());
    }

    public boolean contains(String name)
    {
        return getItem(name) != null;
    }

    public void removeItem(String name)
    {
        T item = getItem(name);
        if (item != null)
        {
            remove(item);
        }
    }

    public ArrayList<String> toNameArray()
    {
        ArrayList<String> names = new ArrayList<>();
        for (T item : this)
        {
            names.add(item.getName());
        }
        return names;
    }
}
