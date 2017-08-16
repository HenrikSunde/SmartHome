package smarthome.smarthome_client.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import smarthome.smarthome_client.arraylists.ShoppinglistItemArrayList;
import smarthome.smarthome_client.models.ShoppingListItem;
import smarthome.smarthome_client.R;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppinglistAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private ShoppinglistItemArrayList mList;

    public ShoppinglistAdapter(@NonNull Context context, @NonNull ShoppinglistItemArrayList items)
    {
        super();
        mInflater = LayoutInflater.from(context);
        mList = items;
    }

    public void add(ShoppingListItem item)
    {
        mList.add(item);
    }

    public void remove(String itemName)
    {
        mList.removeItem(itemName);
    }

    public void clear()
    {
        mList.clear();
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

    @Override
    public ShoppingListItem getItem(int position)
    {
        return mList.get(position);
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
        View customView = mInflater.inflate(R.layout.custom_shoppinglist_row, parent, false);

        ShoppingListItem singleShoppinglistItem = getItem(position);
        TextView shoppinglistItemName = (TextView) customView.findViewById(R.id.shoppinglist_item_name_textview);

        shoppinglistItemName.setText(singleShoppinglistItem.getItemName());

        // Put a strike through the text if the item is marked
        if (singleShoppinglistItem.isMarked())
        {
            shoppinglistItemName.setPaintFlags(shoppinglistItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            shoppinglistItemName.setBackgroundColor(customView.getResources().getColor(R.color.markedGray, null));
        }
        else
        {
            shoppinglistItemName.setPaintFlags(0);
            shoppinglistItemName.setBackgroundColor(customView.getResources().getColor(R.color.transparent, null));
        }

        return customView;
    }
}
