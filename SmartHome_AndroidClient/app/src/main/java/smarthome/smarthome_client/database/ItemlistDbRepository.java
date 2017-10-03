package smarthome.smarthome_client.database;

import android.database.sqlite.SQLiteDatabase;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.database.interfaces.IItemlistRepository;
import smarthome.smarthome_client.models.ItemlistTitleItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistDbRepository implements IItemlistRepository
{
    private DbHelper mDbHelper;

    public ItemlistDbRepository(DbHelper dbHelper)
    {
        mDbHelper = dbHelper;
    }


    @Override
    public void add(ItemlistTitleItem item)
    {
        ItemArraylist<ItemlistTitleItem> list = new ItemArraylist<>();
        list.add(item);
        add(list);
    }

    @Override
    public void add(ItemArraylist<ItemlistTitleItem> items)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.insert_itemlistTitleItems(items);
        }
    }

    @Override
    public ItemlistTitleItem get(String itemlistTitle)
    {
        try (SQLiteDatabase mDb = mDbHelper.getReadableDatabase())
        {
            mDbHelper.setDb(mDb);

            return mDbHelper.get_itemlistTitleItem(itemlistTitle);
        }
    }

    @Override
    public ItemlistTitleItem get(int id)
    {
        try (SQLiteDatabase mDb = mDbHelper.getReadableDatabase())
        {
            mDbHelper.setDb(mDb);

            return mDbHelper.get_itemlistTitleItem(id);
        }
    }

    @Override
    public ItemArraylist<ItemlistTitleItem> get()
    {
        try (SQLiteDatabase mDb = mDbHelper.getReadableDatabase())
        {
            mDbHelper.setDb(mDb);

            return mDbHelper.getAll_itemlistTitleItem();
        }
    }

    @Override
    public void update(ItemlistTitleItem item)
    {
        ItemArraylist<ItemlistTitleItem> items = new ItemArraylist<>();
        items.add(item);
        update(items);
    }


    @Override
    public void update(ItemArraylist<ItemlistTitleItem> items)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.update_itemlistTitleItem(items);
        }
    }

    @Override
    public void delete(ItemlistTitleItem item)
    {
        ItemArraylist<ItemlistTitleItem> items = new ItemArraylist<>();
        items.add(item);
        delete(items);
    }

    @Override
    public void delete(ItemArraylist<ItemlistTitleItem> items)
    {
        try (SQLiteDatabase mDb = mDbHelper.getWritableDatabase())
        {
            mDbHelper.setDb(mDb);

            mDbHelper.delete_itemlistTitleItems(items);
        }
    }
}
