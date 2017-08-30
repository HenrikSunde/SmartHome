package smarthome.smarthome_client.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.arraylists.ItemlistItem_ArrayList;
import smarthome.smarthome_client.clicklisteners.itemlist.Itemlist_ItemClickListener;
import smarthome.smarthome_client.clicklisteners.itemlist.Itemlist_ItemRemove_ButtonClickListener;
import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_Adapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ItemlistItem_ArrayList mList;
    private Comparator<ItemlistItem> mComparator;

    public Itemlist_Adapter(@NonNull Context context, @NonNull ItemlistItem_ArrayList items, Comparator<ItemlistItem> comparator)
    {
        super();
        mInflater = LayoutInflater.from(context);
        mList = items;
        mComparator = comparator;
    }


    public void add(ItemlistItem item)
    {
        mList.add(item);
        Collections.sort(mList, mComparator);
        notifyDataSetChanged();
    }


    public void remove(String itemName)
    {
        mList.removeItem(itemName);
        notifyDataSetChanged();
    }


    public void clear()
    {
        mList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount()
    {
        return mList.size();
    }


    public boolean contains(String itemName)
    {
        return mList.contains(itemName);
    }


    public ItemlistItem_ArrayList getList()
    {
        return mList;
    }


    @Override
    public ItemlistItem getItem(int position)
    {
        return mList.get(position);
    }


    public ItemlistItem getItem(String itemName)
    {
        return mList.getItem(itemName);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.custom_itemlist_row, parent, false);
        }
        TextView itemlistItemName = (TextView) convertView.findViewById(R.id.shoppinglist_item_name_textview);

        // Set onClickListeners
        itemlistItemName.setOnClickListener(new Itemlist_ItemClickListener(this));
        convertView.findViewById(R.id.shoppinglist_item_remove_btn).setOnClickListener(new Itemlist_ItemRemove_ButtonClickListener(this));

        // Set the text of the TextView (itemName)
        ItemlistItem singleItemlistItem = getItem(position);
        itemlistItemName.setText(singleItemlistItem.getItemName());

        // Put a strike through the text if the item is marked
        if (singleItemlistItem.isMarked())
        {
            itemlistItemName.setPaintFlags(itemlistItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemlistItemName.setBackgroundColor(convertView.getResources().getColor(android.R.color.darker_gray, null));
        }
        else
        {
            itemlistItemName.setPaintFlags(0);
            itemlistItemName.setBackgroundColor(convertView.getResources().getColor(android.R.color.transparent, null));
        }

        return convertView;
    }
}
