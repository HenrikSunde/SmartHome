package smarthome.smarthome_client.database.interfaces;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public interface IItemRepository
{
    void add(ItemlistItem item);
    void add(ItemArraylist<ItemlistItem> items);
    ItemArraylist<ItemlistItem> get(int list_id);
    void delete(int list_id);
    void delete(ItemArraylist<ItemlistItem> items);
}
