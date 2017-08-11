package smarthome.smarthome_client.arraylists;

import java.util.ArrayList;

import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemArrayList extends ArrayList<Object>
{
    public boolean contains(String itemName)
    {
        ShoppingListItem item;

        for (java.lang.Object obj : this)
        {
            item = (ShoppingListItem) obj;
            if (item.getItemName().equals(itemName))
            {
                return true;
            }
        }
        return false;
    }

    public Object getItem(String itemName)
    {
        ShoppingListItem item;

        for (java.lang.Object obj : this)
        {
            item = (ShoppingListItem) obj;
            if (item.getItemName().equals(itemName))
            {
                return obj;
            }
        }
        return null;
    }

    public boolean removeItem(String itemName)
    {
        Object obj = getItem(itemName);
        return obj != null && remove(obj);
    }
}
