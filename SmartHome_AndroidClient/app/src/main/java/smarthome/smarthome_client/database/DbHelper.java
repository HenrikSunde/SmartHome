package smarthome.smarthome_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import smarthome.smarthome_client.arraylists.ItemlistItem_ArrayList;
import smarthome.smarthome_client.arraylists.NavigationDrawerItem_ArrayList;
import smarthome.smarthome_client.database.DatabaseContract.DatabaseEntry;
import smarthome.smarthome_client.models.ItemlistItem;
import smarthome.smarthome_client.models.NavigationDrawerItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class DbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";




    private static final String SQL_CREATE_ITEMLISTS =
            "CREATE TABLE " + DatabaseEntry.TABLE_ITEMLISTS + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_ITEMLIST_NAME + " TEXT," +
                    DatabaseEntry.COLUMN_ITEMLIST_ICON + " INTEGER," +
                    DatabaseEntry.COLUMN_ITEMLIST_PUBLIC + " INTEGER)";
    private static final String SQL_DELETE_ITEMLISTS =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_ITEMLISTS;




    private static final String SQL_CREATE_SUGGESTIONS =
            "CREATE TABLE " + DatabaseEntry.TABLE_SUGGESTIONS + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_SUGGESTION_ITEMNAME + " TEXT," +
                    DatabaseEntry.COLUMN_SUGGESTION_LIST + " TEXT)";
//                    DatabaseEntry.COLUMN_SUGGESTION_LIST + " TEXT REFERENCES " + DatabaseEntry.TABLE_ITEMLISTS + "(" + DatabaseEntry.COLUMN_ITEMLIST_NAME + "))";
    private static final String SQL_DELETE_SUGGESTIONS =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_SUGGESTIONS;




    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + DatabaseEntry.TABLE_ITEMS + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_ITEM_NAME + " TEXT," +
                    DatabaseEntry.COLUMN_ITEM_MARKED + " INTEGER," +
                    DatabaseEntry.COLUMN_ITEM_DATEADDED + " TEXT," +
                    DatabaseEntry.COLUMN_ITEM_LIST + " TEXT)";
//                    DatabaseEntry.COLUMN_ITEM_LIST + " TEXT REFERENCES " + DatabaseEntry.TABLE_ITEMLISTS + "(" + DatabaseEntry.COLUMN_ITEMLIST_NAME + "))";
    private static final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_ITEMS;




    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public DbHelper(Context context)
    {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    public static void deleteItemLists(SQLiteDatabase db)
    {
        Log.i("TEST", "In deleteItemLists()");
        db.delete(DatabaseEntry.TABLE_ITEMLISTS, null, null);
    }

    public static void deleteItemlist(SQLiteDatabase db, String itemlistName)
    {
        Log.i("TEST", "In deleteItemList()");
        deleteItemsInItemlist(db, itemlistName);
        deleteSuggestionsForItemlist(db, itemlistName);
        db.delete(DatabaseEntry.TABLE_ITEMLISTS, DatabaseEntry.COLUMN_ITEMLIST_NAME + "=?", new String[]{itemlistName});
    }

    public static void deleteItemsInItemlist(SQLiteDatabase db, String itemlistName)
    {
        Log.i("TEST", "In deleteItemsInItemlist()");
        db.delete(DatabaseEntry.TABLE_ITEMS, DatabaseEntry.COLUMN_ITEM_LIST + "=?", new String[]{itemlistName});
    }

    public static void deleteSuggestionsForItemlist(SQLiteDatabase db, String itemlistName)
    {
        Log.i("TEST", "In deleteSuggestionsForItemList()");
        db.delete(DatabaseEntry.TABLE_SUGGESTIONS, DatabaseEntry.COLUMN_SUGGESTION_LIST + "=?", new String[]{itemlistName});
    }





    public static NavigationDrawerItem_ArrayList getItemLists(SQLiteDatabase db)
    {
        Log.i("TEST", "In getItemLists()");
        Cursor cursor = db.query(
                DatabaseEntry.TABLE_ITEMLISTS,
                null,
                null,
                null,
                null,
                null,
                null);

        NavigationDrawerItem_ArrayList returnList = new NavigationDrawerItem_ArrayList();
        while (cursor.moveToNext())
        {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_NAME));
            int icon = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_ICON));
            boolean publicList = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC)) == 1;

            NavigationDrawerItem item = new NavigationDrawerItem(itemName, icon, publicList);
            returnList.add(item);

            Log.i("TEST", "*** getItemLists() *** - itemName=" + itemName + " ::: icon=" + icon);
        }
        cursor.close();
        return returnList;
    }


    public static ItemlistItem_ArrayList getItemsForList(SQLiteDatabase db, String listName)
    {
        Log.i("TEST", "In getItemsForList()");
        Cursor cursor = db.query(
                DatabaseEntry.TABLE_ITEMS,
                null,
                DatabaseEntry.COLUMN_ITEM_LIST + "=?",
                new String[]{listName},
                null,
                null,
                null);

        ItemlistItem_ArrayList returnList = new ItemlistItem_ArrayList();
        while (cursor.moveToNext())
        {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_SUGGESTION_ITEMNAME));
            boolean marked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_MARKED)) == 1;
            String addedDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEM_DATEADDED));

            ItemlistItem item = new ItemlistItem(itemName, marked, addedDate);
            returnList.add(item);
            Log.i("TEST", "*** getItemsForList() *** - listName=" + listName + " ::: itemName=" + itemName + " ::: marked=" + marked + " ::: addedDate=" + addedDate);
        }
        cursor.close();
        return returnList;
    }


    public static ArrayList<String> getSuggestionsForList(SQLiteDatabase db, String listName)
    {
        Log.i("TEST", "In getSuggestionsForList()");
        Cursor cursor = db.query(
                DatabaseEntry.TABLE_SUGGESTIONS,
                null,
                DatabaseEntry.COLUMN_SUGGESTION_LIST + "=?",
                new String[]{listName},
                null,
                null,
                DatabaseEntry.COLUMN_SUGGESTION_ITEMNAME + " " + DatabaseEntry.ASCENDING);

        ArrayList<String> returnList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_SUGGESTION_ITEMNAME));

            returnList.add(itemName);
            Log.i("TEST", "*** getSuggestionsForList() *** - listName=" + listName + " ::: itemName=" + itemName);
        }
        cursor.close();
        return returnList;
    }







    public static void insertAllItemLists(SQLiteDatabase db, NavigationDrawerItem_ArrayList itemLists)
    {
        Log.i("TEST", "In insertAllItemLists()");
        ContentValues values;
        for (NavigationDrawerItem item : itemLists)
        {
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_ITEMLIST_NAME, item.getItemName());
            values.put(DatabaseEntry.COLUMN_ITEMLIST_ICON, item.getIconResourceID());
            values.put(DatabaseEntry.COLUMN_ITEMLIST_PUBLIC, item.isPublicList() ? 1 : 0);
            long newRowId = db.insert(DatabaseEntry.TABLE_ITEMLISTS, null, values);
            Log.i("TEST", "*** insertAllItemLists() *** - rowId= " + newRowId + " ::: itemName=" + item.getItemName() + " ::: icon=" + item.getIconResourceID() + " ::: public=" + item.isPublicList());
        }
    }

    public static void insertAllItems(SQLiteDatabase db, ItemlistItem_ArrayList items, String listName)
    {
        Log.i("TEST", "In insertAllItems()");
        ContentValues values;
        for (ItemlistItem item : items)
        {
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_ITEM_NAME, item.getItemName());
            values.put(DatabaseEntry.COLUMN_ITEM_MARKED, item.isMarked() ? 1 : 0);
            values.put(DatabaseEntry.COLUMN_ITEM_DATEADDED, item.getAddDateFormatted());
            values.put(DatabaseEntry.COLUMN_ITEM_LIST, listName);
            long newRowId = db.insert(DatabaseEntry.TABLE_ITEMS, null, values);
            Log.i("TEST", "*** insertAllItems() *** - rowId= " + newRowId + " ::: itemName=" + item.getItemName() + " ::: marked=" + item.isMarked() + " ::: addedDate=" + item.getAddDateFormatted() + " ::: listName=" + listName);
        }
    }

    public static void insertAllSuggestions(SQLiteDatabase db, ArrayList<String> suggestions, String listName)
    {
        Log.i("TEST", "In insertAllSuggestions()");
        ContentValues values;
        for (String suggestion : suggestions)
        {
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_SUGGESTION_ITEMNAME, suggestion);
            values.put(DatabaseEntry.COLUMN_SUGGESTION_LIST, listName);
            long newRowId = db.insert(DatabaseEntry.TABLE_SUGGESTIONS, null, values);
            Log.i("TEST", "*** insertAllSuggestions() *** - rowId= " + newRowId + " ::: suggestion=" + suggestion + " ::: listName=" + listName);
        }
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
}
