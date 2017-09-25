package smarthome.smarthome_client.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.exceptions.NotImplementedException;
import smarthome.smarthome_client.models.ItemlistItem;
import smarthome.smarthome_client.models.interfaces.NameableItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_Adapter extends SmartHomeBaseAdapter<ItemlistItem>
{
    private Activity mActivity;
    private LayoutInflater mInflater;

    public Itemlist_Adapter(Activity activity, ItemArraylist<ItemlistItem> list, Comparator<ItemlistItem> sortListWith_Comparator)
    {
        super(list, sortListWith_Comparator);

        mActivity = activity;
        mInflater = LayoutInflater.from(mActivity.getApplicationContext());
    }

    @Override
    public void editItem(ItemlistItem originalItem, ItemlistItem newItem)
    {
        throw new NotImplementedException();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.custom_itemlist_row, parent, false);
        }

        // Find views
        TextView itemlistItemName = (TextView) convertView.findViewById(R.id.shoppinglist_item_name_textview);
        Button removeButton = (Button) convertView.findViewById(R.id.shoppinglist_item_remove_btn);

        // Set onClickListeners
        itemlistItemName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView item_textView = (TextView) v.findViewById(R.id.shoppinglist_item_name_textview);

                String itemName = item_textView.getText().toString();
                ItemlistItem item = getItem(itemName);

                // Switch between marked and unmarked
                item.setMarked(!item.isMarked());

                notifyDataSetChanged();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ViewGroup parent = (ViewGroup) v.getParent();
                TextView item_textView = (TextView) parent.findViewById(R.id.shoppinglist_item_name_textview);

                String itemName = item_textView.getText().toString();
                remove(itemName);
                notifyDataSetChanged();
            }
        });

        // Set the text of the TextView (itemName)
        ItemlistItem singleItemlistItem = getItem(position);
        itemlistItemName.setText(singleItemlistItem.getName());

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
