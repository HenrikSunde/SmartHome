package smarthome.smarthome_client.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import smarthome.smarthome_client.models.ShoppingListItem;
import smarthome.smarthome_client.R;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppinglistAdapter extends ArrayAdapter<Object>
{
    public ShoppinglistAdapter(@NonNull Context context, @NonNull List<Object> objects)
    {
        super(context, R.layout.custom_shoppinglist_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_shoppinglist_row, parent, false);

        ShoppingListItem singleShoppinglistItem = (ShoppingListItem) getItem(position);
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