package smarthome.smarthome_client.database.interfaces;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.models.ItemlistTitleItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public interface IItemlistRepository
{
    void add(ItemlistTitleItem item);
    void add(ItemArraylist<ItemlistTitleItem> items);
    ItemlistTitleItem get(String itemlistTitle);
    ItemlistTitleItem get(int id);
    ItemArraylist<ItemlistTitleItem> get();
    void update(ItemlistTitleItem item);
    void update(ItemArraylist<ItemlistTitleItem> items);
    void delete(ItemlistTitleItem item);
    void delete(ItemArraylist<ItemlistTitleItem> items);
    int count(int list_id);
}
