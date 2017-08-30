package smarthome.smarthome_client.arraylists;

import java.util.ArrayList;

import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistItem_ArrayList extends ArrayList<ItemlistItem>
{
    public ItemlistItem getItem(String itemName)
    {
        for (ItemlistItem item : this)
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
        ItemlistItem item = getItem(itemName);
        return item != null && remove(item);
    }
}
