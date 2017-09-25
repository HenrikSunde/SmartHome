package smarthome.smarthome_client.database;

import android.provider.BaseColumns;

/***************************************************************************************************
 *
 **************************************************************************************************/
public final class DatabaseContract
{
    private DatabaseContract()
    {
    }

    public static class DatabaseEntry implements BaseColumns
    {
        // Sorting options
        public static final String DESCENDING = "DESC";
        public static final String ASCENDING = "ASC";

        // Table suggestions
        public static final String TABLE_SUGGESTIONS = "suggestions";
        public static final String COLUMN_SUGGESTION_NAME = "name";
        public static final String COLUMN_SUGGESTION_LIST = "itemlist";

        // Table items
        public static final String TABLE_ITEMS = "items";
        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_ITEM_MARKED = "marked";
        public static final String COLUMN_ITEM_DATEADDED = "dateadded";
        public static final String COLUMN_ITEM_LIST = "itemlist";

        // Table itemlists
        public static final String TABLE_ITEMLISTS = "itemlists";
        public static final String COLUMN_ITEMLIST_NAME = "name";
        public static final String COLUMN_ITEMLIST_ICON = "icon";
        public static final String COLUMN_ITEMLIST_PUBLIC = "publicList";
        public static final String COLUMN_ITEMLIST_SUGGESTIONS = "suggestions";
    }
}