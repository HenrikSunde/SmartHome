package smarthome.smarthome_client.database;

import android.database.sqlite.SQLiteDatabase;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.database.interfaces.ISuggestionRepository;
import smarthome.smarthome_client.models.Suggestion;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class SuggestionDbRepository implements ISuggestionRepository
{
    private DbHelper mDbHelper;

    public SuggestionDbRepository(DbHelper dbHelper)
    {
        mDbHelper = dbHelper;
    }


    @Override
    public void add(Suggestion suggestion)
    {
        ItemArraylist<Suggestion> list = new ItemArraylist<>();
        list.add(suggestion);
        add(list);
    }

    @Override
    public void add(ItemArraylist<Suggestion> suggestions)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.insert_suggestions(suggestions);
        }
    }

    @Override
    public ItemArraylist<Suggestion> get(int list_id)
    {
        try (SQLiteDatabase mDb = mDbHelper.getReadableDatabase())
        {
            mDbHelper.setDb(mDb);

            return mDbHelper.get_suggestions(list_id);
        }
    }

    @Override
    public Suggestion get(int list_id, String suggestionName)
    {
        try (SQLiteDatabase mDb = mDbHelper.getReadableDatabase())
        {
            mDbHelper.setDb(mDb);

            return mDbHelper.get_suggestion(list_id, suggestionName);
        }
    }

    @Override
    public void delete(int list_id)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.delete_suggestions(list_id);
        }
    }

    @Override
    public void delete(Suggestion suggestion)
    {
        ItemArraylist<Suggestion> suggestions = new ItemArraylist<>();
        suggestions.add(suggestion);
        delete(suggestions);
    }

    @Override
    public void delete(ItemArraylist<Suggestion> suggestions)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.delete_suggestions(suggestions);
        }
    }
}
