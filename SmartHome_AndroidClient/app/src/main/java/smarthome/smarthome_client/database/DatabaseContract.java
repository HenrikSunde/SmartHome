package smarthome.smarthome_client.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/***************************************************************************************************
 *
 **************************************************************************************************/
public final class DatabaseContract
{
    private DatabaseContract(){}

    public static class DatabaseEntry implements BaseColumns
    {
        public static final String TABLE_SHOPPINGLISTITEM = "shoppinglist";
        public static final String COLUMN_ITEMNAME = "itemname";
        public static final String COLUMN_ITEMMARKED = "itemmarked";
    }
}