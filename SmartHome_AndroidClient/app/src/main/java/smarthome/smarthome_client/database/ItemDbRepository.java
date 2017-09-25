package smarthome.smarthome_client.database;

import android.database.sqlite.SQLiteDatabase;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.database.interfaces.IItemRepository;
import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemDbRepository implements IItemRepository
{
    private DbHelper mDbHelper;

    public ItemDbRepository(DbHelper dbHelper)
    {
        mDbHelper = dbHelper;
    }


    @Override
    public void add(ItemlistItem item)
    {
        ItemArraylist<ItemlistItem> list = new ItemArraylist<>();
        list.add(item);
        add(list);
    }

    @Override
    public void add(ItemArraylist<ItemlistItem> items)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.insert_itemlistItems(items);
        }
    }

    @Override
    public ItemArraylist<ItemlistItem> get(int list_id)
    {
        try (SQLiteDatabase mDb = mDbHelper.getReadableDatabase())
        {
            mDbHelper.setDb(mDb);

            return mDbHelper.get_itemlistItems(list_id);
        }
    }

    @Override
    public void delete(int list_id)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.delete_itemlistItems(list_id);
        }
    }

    @Override
    public void delete(ItemArraylist<ItemlistItem> items)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.delete_itemlistItems(items);
        }
    }
}
