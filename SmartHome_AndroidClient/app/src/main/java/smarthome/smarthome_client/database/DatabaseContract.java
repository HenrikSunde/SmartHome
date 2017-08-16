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
        public static final String DESCENDING = "DESC";
        public static final String ASCENDING = "ASC";

        public static final String TABLE_ITEMSUGGESTIONS = "itemsuggestions";
        public static final String COLUMN_IS_ITEMNAME = "itemname";

        public static final String TABLE_DEFAULTSHOPPINGLIST = "defaultshoppinglist";
        public static final String COLUMN_DSH_ITEMNAME = "itemname";
        public static final String COLUMN_DSH_ITEMMARKED = "itemmarked";
    }
}