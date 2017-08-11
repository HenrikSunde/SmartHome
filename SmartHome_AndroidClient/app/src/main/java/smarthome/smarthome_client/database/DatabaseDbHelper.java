package smarthome.smarthome_client.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class DatabaseDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";

    private static final String SQL_CREATE_SHOPPINGLISTITEMS =
            "CREATE TABLE " + DatabaseContract.DatabaseEntry.TABLE_SHOPPINGLISTITEM + " (" +
                    DatabaseContract.DatabaseEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.DatabaseEntry.COLUMN_ITEMNAME + " TEXT," +
                    DatabaseContract.DatabaseEntry.COLUMN_ITEMMARKED + " INTEGER)";

    private static final String SQL_DELETE_SHOPPINGLISTITEMS =
            "DROP TABLE IF EXISTS " + DatabaseContract.DatabaseEntry.TABLE_SHOPPINGLISTITEM;

    public DatabaseDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public static DatabaseDbHelper getDbHelper(Context context)
    {
        return new DatabaseDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_SHOPPINGLISTITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_SHOPPINGLISTITEMS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}
