package smarthome.smarthome_client.database.interfaces;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.models.Suggestion;

/***************************************************************************************************
 *
 **************************************************************************************************/
public interface ISuggestionRepository
{
    void add(Suggestion suggestion);
    void add(ItemArraylist<Suggestion> suggestions);
    ItemArraylist<Suggestion> get(int list_id);
    Suggestion get(int list_id, String suggestionName);
    void delete(int list_id);
    void delete(Suggestion suggestion);
    void delete(ItemArraylist<Suggestion> suggestions);
}
