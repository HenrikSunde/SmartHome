package smarthome.smarthome_client.arraylists;

import java.util.ArrayList;

import smarthome.smarthome_client.models.NavigationDrawerItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class NavigationDrawerItem_ArrayList extends ArrayList<NavigationDrawerItem>
{
    public NavigationDrawerItem getItem(String itemName)
    {
        for (NavigationDrawerItem item : this)
        {
            if (item.getItemName().equals(itemName))
            {
                return item;
            }
        }
        return null;
    }

    public boolean contains(String itemName)
    {
        return getItem(itemName) != null;
    }

    public boolean removeItem(String itemName)
    {
        NavigationDrawerItem item = getItem(itemName);
        return item != null && remove(item);
    }
}
