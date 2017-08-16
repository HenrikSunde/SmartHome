package smarthome.smarthome_client.arraylists;

import java.util.ArrayList;

import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppinglistItemArrayList extends ArrayList<ShoppingListItem>
{
    public ShoppingListItem getItem(String itemName)
    {
        for (ShoppingListItem item : this)
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
        ShoppingListItem item = getItem(itemName);
        return item != null && remove(item);
    }
}
