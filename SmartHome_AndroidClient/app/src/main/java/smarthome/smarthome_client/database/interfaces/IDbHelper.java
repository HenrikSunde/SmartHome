package smarthome.smarthome_client.database.interfaces;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.models.ItemlistItem;
import smarthome.smarthome_client.models.ItemlistTitleItem;
import smarthome.smarthome_client.models.Suggestion;

/***************************************************************************************************
 *
 **************************************************************************************************/
public interface IDbHelper
{
    // ItemlistTitleItem - The content of the navigation drawer
    void insert_itemlistTitleItems(ItemArraylist<ItemlistTitleItem> items);
    ItemlistTitleItem get_itemlistTitleItem(String itemlistTitle);
    ItemlistTitleItem get_itemlistTitleItem(int list_id);
    ItemArraylist<ItemlistTitleItem> getAll_itemlistTitleItem();
    void update_itemlistTitleItem(ItemArraylist<ItemlistTitleItem> items);
    void delete_itemlistTitleItems(ItemArraylist<ItemlistTitleItem> items);
    int get_itemlistItemCount(int list_id);


    // Suggestions - The content of the autoComplete-TextView
    void insert_suggestions(ItemArraylist<Suggestion> suggestions);
    ItemArraylist<Suggestion> get_suggestions(int list_id);
    Suggestion get_suggestion(int list_id, String suggestionName);
    void delete_suggestions(int list_id);
    void delete_suggestions(ItemArraylist<Suggestion> suggestions);


    // Listitems - The content of the itemlists
    void insert_itemlistItems(ItemArraylist<ItemlistItem> items);
    ItemArraylist<ItemlistItem> get_itemlistItems(int list_id);
    ItemlistItem get_itemlistItem(int list_id, String itemName);
    void delete_itemlistItems(int list_id);
    void delete_itemlistItems(ItemArraylist<ItemlistItem> items);
}
