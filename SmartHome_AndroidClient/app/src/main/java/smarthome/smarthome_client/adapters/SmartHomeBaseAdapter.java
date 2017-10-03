package smarthome.smarthome_client.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;

import java.util.Collections;
import java.util.Comparator;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.models.interfaces.NameableItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public abstract class SmartHomeBaseAdapter<T extends NameableItem> extends BaseAdapter
{
    private ItemArraylist<T> mList;
    private Comparator<T> mComparator;

    public SmartHomeBaseAdapter(ItemArraylist<T> list, Comparator<T> sortListWith_Comparator)
    {
        super();
        mList = list;
        mComparator = sortListWith_Comparator;
    }

    public void add(T item)
    {
        mList.add(item);
        Collections.sort(mList, mComparator);
    }

    public void remove(T item)
    {
        mList.remove(item);
    }

    public void remove(String itemName)
    {
        mList.removeItem(itemName);
    }

    public void clear()
    {
        mList.clear();
    }

    public boolean contains(T item)
    {
        return mList.contains(item);
    }

    public boolean contains(String itemName)
    {
        return mList.contains(itemName);
    }

    public ItemArraylist<T> getList()
    {
        return mList;
    }

    public void setList(ItemArraylist<T> list)
    {
        clear();
        for (T item : list)
        {
            mList.add(item);
        }
        Collections.sort(mList, mComparator);
    }

    public T getItem(String itemName)
    {
        return mList.getItem(itemName);
    }

    public abstract void editItem(T originalItem, T newItem);

    public int getPosition(String itemName)
    {
        return mList.getPosition(itemName);
    }

    public int getPosition(T item)
    {
        return mList.getPosition(item);
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public T getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return -1;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
