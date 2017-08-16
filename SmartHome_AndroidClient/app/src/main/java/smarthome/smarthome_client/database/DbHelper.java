package smarthome.smarthome_client.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import smarthome.smarthome_client.arraylists.ShoppinglistItemArrayList;
import smarthome.smarthome_client.database.DatabaseContract.DatabaseEntry;
import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class DbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";


    private static final String SQL_CREATE_ITEMSUGGESTIONS =
            "CREATE TABLE " + DatabaseEntry.TABLE_ITEMSUGGESTIONS + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_IS_ITEMNAME + " TEXT)";

    private static final String SQL_DELETE_ITEMSUGGESTIONS =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_ITEMSUGGESTIONS;


    private static final String SQL_CREATE_DEFAULTSHOPPINGLIST =
            "CREATE TABLE " + DatabaseEntry.TABLE_DEFAULTSHOPPINGLIST + " (" +
                    DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseEntry.COLUMN_DSH_ITEMNAME + " TEXT," +
                    DatabaseEntry.COLUMN_DSH_ITEMMARKED + " INTEGER)";

    private static final String SQL_DELETE_DEFAULTSHOPPINGLIST =
            "DROP TABLE IF EXISTS " + DatabaseEntry.TABLE_DEFAULTSHOPPINGLIST;




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
        db.execSQL(SQL_CREATE_ITEMSUGGESTIONS);
        db.execSQL(SQL_CREATE_DEFAULTSHOPPINGLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ITEMSUGGESTIONS);
        db.execSQL(SQL_DELETE_DEFAULTSHOPPINGLIST);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }


    /**
     * Returns a table as an ArrayList containing each row as an individual object
     * */
    public static ShoppinglistItemArrayList getAllShoppinglistItemEntries(SQLiteDatabase db, String tableName, String orderBy)
    {
        Cursor cursor = db.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                orderBy);

        ShoppinglistItemArrayList returnArrayList = new ShoppinglistItemArrayList();
        while (cursor.moveToNext())
        {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_DSH_ITEMNAME));
            boolean itemMarked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_DSH_ITEMMARKED)) == 1;

            returnArrayList.add(new ShoppingListItem(itemName, itemMarked));
        }
        cursor.close();
        return returnArrayList;
    }

    public static ArrayList<String> getAllSuggestionEntries(SQLiteDatabase db)
    {
        Cursor cursor = db.query(
                DatabaseEntry.TABLE_ITEMSUGGESTIONS,
                null,
                null,
                null,
                null,
                null,
                DatabaseEntry.COLUMN_IS_ITEMNAME + " " + DatabaseEntry.ASCENDING);

        ArrayList<String> returnArrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_IS_ITEMNAME));

            returnArrayList.add(itemName);
        }
        cursor.close();
        return returnArrayList;
    }


    /**
     * Clears all data from a table
     * */
    public static void clearAllEntries(SQLiteDatabase db, String tableName)
    {
        db.execSQL("DELETE FROM " + tableName);
    }

    /**
     * Inserts entryList into a table
     * */
    public static void insertAllSuggestionEntries(SQLiteDatabase db, String tableName, ArrayList<String> entryList)
    {
        ContentValues values;

        for (String suggestion : entryList)
        {
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_IS_ITEMNAME, suggestion);
            long newRowId = db.insert(tableName, null, values);
        }
    }

    public static void insertAllShoppinglistItemEntries(SQLiteDatabase db, String tableName, ShoppinglistItemArrayList entryList)
    {
        ContentValues values;
        for (ShoppingListItem item : entryList)
        {
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_DSH_ITEMNAME, item.getItemName());
            values.put(DatabaseEntry.COLUMN_DSH_ITEMMARKED, item.isMarked() ? 1 : 0);
            long newRowId = db.insert(tableName, null, values);
        }
    }
}
