package smarthome.smarthome_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.database.DatabaseContract.DatabaseEntry;
import smarthome.smarthome_client.database.interfaces.IDbHelper;
import smarthome.smarthome_client.exceptions.NotImplementedException;
import smarthome.smarthome_client.models.ItemlistItem;
import smarthome.smarthome_client.models.ItemlistTitleItem;
import smarthome.smarthome_client.models.Suggestion;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class DbHelper extends SQLiteOpenHelper implements IDbHelper
{
    private SQLiteDatabase mDb;

    public SQLiteDatabase getDb()
    {
        return mDb;
    }

    void setDb(SQLiteDatabase mDb)
    {
        this.mDb = mDb;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "smarthome.db";




    private static final String SQL_CREATE_ITEMLISTS = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY" +
                    ", %s TEXT UNIQUE" +
                    ", %s INTEGER" +
                    ", %s INTEGER" +
                    ", %s INTEGER)"
            , DatabaseEntry.TABLE_ITEMLISTS
            , DatabaseEntry._ID
            , DatabaseEntry.COLUMN_ITEMLIST_NAME
            , DatabaseEntry.COLUMN_ITEMLIST_ICON
            , DatabaseEntry.COLUMN_ITEMLIST_PUBLIC
            , DatabaseEntry.COLUMN_ITEMLIST_SUGGESTIONS);

    private static final String SQL_DELETE_ITEMLISTS = String.format(
            "DROP TABLE IF EXISTS %s"
            , DatabaseEntry.TABLE_ITEMLISTS);




    private static final String SQL_CREATE_SUGGESTIONS = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY" +
                    ", %s TEXT" +
                    ", %s INTEGER REFERENCES %s(%s))"
            , DatabaseEntry.TABLE_SUGGESTIONS
            , DatabaseEntry._ID
            , DatabaseEntry.COLUMN_SUGGESTION_NAME
            , DatabaseEntry.COLUMN_SUGGESTION_LIST
            , DatabaseEntry.TABLE_ITEMLISTS
            , DatabaseEntry._ID);

    private static final String SQL_DELETE_SUGGESTIONS = String.format(
            "DROP TABLE IF EXISTS %s"
            , DatabaseEntry.TABLE_SUGGESTIONS);




    private static final String SQL_CREATE_ITEMS = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY" +
                    ", %s TEXT" +
                    ", %s INTEGER" +
                    ", %s TEXT" +
                    ", %s INTEGER REFERENCES %s(%s))"
            , DatabaseEntry.TABLE_ITEMS
            , DatabaseEntry._ID
            , DatabaseEntry.COLUMN_ITEM_NAME
            , DatabaseEntry.COLUMN_ITEM_MARKED
            , DatabaseEntry.COLUMN_ITEM_DATEADDED
            , DatabaseEntry.COLUMN_ITEM_LIST
            , DatabaseEntry.TABLE_ITEMLISTS
            , DatabaseEntry._ID);

    private static final String SQL_DELETE_ITEMS = String.format(
            "DROP TABLE IF EXISTS %s"
            , DatabaseEntry.TABLE_ITEMS);




    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public DbHelper(Context context)
    {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }






    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ITEMLISTS);
        db.execSQL(SQL_CREATE_SUGGESTIONS);
        db.execSQL(SQL_CREATE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_SUGGESTIONS);
        db.execSQL(SQL_DELETE_ITEMS);
        db.execSQL(SQL_DELETE_ITEMLISTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }





    /**
     * ItemlistTitleItem - The content of the navigation drawer
     * */
    @Override
    public void insert_itemlistTitleItems(ItemArraylist<ItemlistTitleItem> items)
    {
        ContentValues values;
        for (ItemlistTitleItem item : items)
        {
            values = new ContentValues();
            // _ID will automatically be inserted as a unique integer
            values.put(DatabaseEntry.COLUMN_ITEMLIST_NAME, item.getName());
            values.put(DatabaseEntry.COLUMN_ITEMLIST_ICON, item.getIconResourceID());
            values.put(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC, item.isPublicList() ? 1 : 0);
            values.put(DatabaseEntry.COLUMN_ITEMLIST_SUGGESTIONS, item.isSuggestions() ? 1 : 0);

            long newRowId = mDb.insert(DatabaseEntry.TABLE_ITEMLISTS, null, values);
        }
    }

    @Override
    public ItemlistTitleItem get_itemlistTitleItem(String itemlistTitle)
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_ITEMLISTS
                , null
                , DatabaseEntry.COLUMN_ITEMLIST_NAME + "=?"
                , new String[]{itemlistTitle}
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_NAME));
                int icon = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_ICON));
                boolean publicList = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC)) == 1;
                boolean suggestions = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_SUGGESTIONS)) == 1;
                return new ItemlistTitleItem(id, name, icon, publicList, suggestions);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public ItemlistTitleItem get_itemlistTitleItem(int list_id)
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_ITEMLISTS
                , null
                , DatabaseEntry._ID + "=?"
                , new String[]{String.valueOf(list_id)}
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_NAME));
                int icon = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_ICON));
                boolean publicList = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC)) == 1;
                boolean suggestions = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_SUGGESTIONS)) == 1;
                return new ItemlistTitleItem(id, name, icon, publicList, suggestions);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public ItemArraylist<ItemlistTitleItem> getAll_itemlistTitleItem()
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_ITEMLISTS
                , null
                , null
                , null
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                ItemArraylist<ItemlistTitleItem> returnList = new ItemArraylist<>();
                while (!cursor.isAfterLast())
                {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_NAME));
                    int icon = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_ICON));
                    boolean publicList = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC)) == 1;
                    boolean suggestions = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_SUGGESTIONS)) == 1;
                    returnList.add(new ItemlistTitleItem(id, name, icon, publicList, suggestions));
                    cursor.moveToNext();
                }
                return returnList;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public void update_itemlistTitleItem(ItemArraylist<ItemlistTitleItem> items)
    {
        ContentValues values;
        for (ItemlistTitleItem item : items)
        {
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_ITEMLIST_NAME, item.getName());
            values.put(DatabaseEntry.COLUMN_ITEMLIST_ICON, item.getIconResourceID());
            values.put(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC, item.isPublicList() ? 1 : 0);
            values.put(DatabaseEntry.COLUMN_ITEMLIST_SUGGESTIONS, item.isSuggestions() ? 1 : 0);

            long newRowId = mDb.update(DatabaseEntry.TABLE_ITEMLISTS, values, DatabaseEntry._ID + "=?", new String[]{ String.valueOf(item.getId()) });
        }
    }

    @Override
    public void delete_itemlistTitleItems(ItemArraylist<ItemlistTitleItem> items)
    {
        for (ItemlistTitleItem item : items)
        {
            delete_suggestions(item.getId());
            delete_itemlistItems(item.getId());
            mDb.delete(DatabaseEntry.TABLE_ITEMLISTS, DatabaseEntry._ID + "=?", new String[]{ String.valueOf(item.getId()) });
        }
    }


    /**
     * Suggestions - The content of the autoComplete-TextView
     * */
    @Override
    public void insert_suggestions(ItemArraylist<Suggestion> suggestions)
    {
        ContentValues values;
        for (Suggestion suggestion : suggestions)
        {
            values = new ContentValues();
            // _ID will automatically be inserted as a unique integer
            values.put(DatabaseEntry.COLUMN_SUGGESTION_NAME, suggestion.getName());
            values.put(DatabaseEntry.COLUMN_SUGGESTION_LIST, suggestion.getList_id());

            long newRowId = mDb.insert(DatabaseEntry.TABLE_SUGGESTIONS, null, values);
        }
    }

    @Override
    public ItemArraylist<Suggestion> get_suggestions(int list_id)
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_SUGGESTIONS
                , null
                , DatabaseEntry.COLUMN_SUGGESTION_LIST + "=?"
                , new String[]{ String.valueOf(list_id) }
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                ItemArraylist<Suggestion> returnList = new ItemArraylist<>();
                while (!cursor.isAfterLast())
                {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_SUGGESTION_NAME));
                    returnList.add(new Suggestion(id, name, list_id));
                    cursor.moveToNext();
                }
                return returnList;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public Suggestion get_suggestion(int list_id, String suggestionName)
    {

        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_SUGGESTIONS
                , null
                , DatabaseEntry.COLUMN_SUGGESTION_LIST + "=? AND " + DatabaseEntry.COLUMN_SUGGESTION_NAME + "=?"
                , new String[]{ String.valueOf(list_id), suggestionName }
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                return new Suggestion(id, suggestionName, list_id);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public void delete_suggestions(int list_id)
    {
        mDb.delete(DatabaseEntry.TABLE_SUGGESTIONS, DatabaseEntry._ID + "=?", new String[]{ String.valueOf(list_id) });
    }

    @Override
    public void delete_suggestions(ItemArraylist<Suggestion> suggestions)
    {
        for (Suggestion suggestion : suggestions)
        {
            mDb.delete(DatabaseEntry.TABLE_SUGGESTIONS, DatabaseEntry._ID + "=?", new String[]{ String.valueOf(suggestion.getList_id()) });
        }
    }


    /**
     * Listitems - The content of the itemlists
     * */
    @Override
    public void insert_itemlistItems(ItemArraylist<ItemlistItem> items)
    {
        ContentValues values;
        for (ItemlistItem item : items)
        {
            values = new ContentValues();
            // _ID will automatically be inserted as a unique integer
            values.put(DatabaseEntry.COLUMN_ITEM_NAME, item.getName());
            values.put(DatabaseEntry.COLUMN_ITEM_MARKED, item.isMarked() ? 1 : 0);
            values.put(DatabaseEntry.COLUMN_ITEM_DATEADDED, item.getDateAddedFormatted());
            values.put(DatabaseEntry.COLUMN_SUGGESTION_LIST, item.getList_id());

            long newRowId = mDb.insert(DatabaseEntry.TABLE_ITEMS, null, values);
        }
    }

    @Override
    public ItemArraylist<ItemlistItem> get_itemlistItems(int list_id)
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_ITEMS
                , null
                , DatabaseEntry.COLUMN_ITEM_LIST + "=?"
                , new String[]{ String.valueOf(list_id) }
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                ItemArraylist<ItemlistItem> returnList = new ItemArraylist<>();
                while (!cursor.isAfterLast())
                {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_NAME));
                    boolean marked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_MARKED)) == 1;
                    String dateAdded = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_DATEADDED));
                    returnList.add(new ItemlistItem(id, name, marked, dateAdded, list_id));
                    cursor.moveToNext();
                }
                return returnList;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public ItemlistItem get_itemlistItem(int list_id, String itemName)
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_ITEMS
                , null
                , DatabaseEntry.COLUMN_ITEM_LIST + "=? AND " + DatabaseEntry.COLUMN_ITEM_NAME + "=?"
                , new String[]{ String.valueOf(list_id), itemName }
                , null
                , null
                , null))
        {
            try
            {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry._ID));
                boolean marked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_MARKED)) == 1;
                String dateAdded = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_DATEADDED));
                return new ItemlistItem(id, itemName, marked, dateAdded, list_id);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public void delete_itemlistItems(int list_id)
    {
        mDb.delete(DatabaseEntry.TABLE_ITEMS, DatabaseEntry._ID + "=?", new String[]{ String.valueOf(list_id) });
    }

    @Override
    public void delete_itemlistItems(ItemArraylist<ItemlistItem> items)
    {
        for (ItemlistItem item : items)
        {
            mDb.delete(DatabaseEntry.TABLE_SUGGESTIONS, DatabaseEntry._ID + "=?", new String[]{ String.valueOf(item.getList_id()) });
        }
    }

    @Override
    public int get_itemlistItemCount(int list_id)
    {
        try (Cursor cursor = mDb.query(
                DatabaseEntry.TABLE_ITEMS,
                null,
                DatabaseEntry.COLUMN_ITEM_LIST + "=?",
                new String[]{ (String.valueOf(list_id)) },
                null,
                null,
                null))
        {
            try
            {
                cursor.moveToFirst();
                return cursor.getCount();
            }
            catch (Exception e)
            {
                return 0;
            }
        }
    }
}
