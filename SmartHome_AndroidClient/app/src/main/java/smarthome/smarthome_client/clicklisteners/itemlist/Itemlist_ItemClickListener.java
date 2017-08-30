package smarthome.smarthome_client.clicklisteners.itemlist;

import android.view.View;
import android.widget.TextView;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.adapters.Itemlist_Adapter;
import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_ItemClickListener implements TextView.OnClickListener
{
    private Itemlist_Adapter mItemlistAdapter;

    public Itemlist_ItemClickListener(Itemlist_Adapter itemlistAdapter)
    {
        this.mItemlistAdapter = itemlistAdapter;
    }

    @Override
    public void onClick(View view)
    {
        TextView item_textView = (TextView) view.findViewById(R.id.shoppinglist_item_name_textview);

        String itemName = item_textView.getText().toString();
        ItemlistItem item = mItemlistAdapter.getItem(itemName);

        // Switch between marked and unmarked
        item.setMarked(!item.isMarked());

        mItemlistAdapter.notifyDataSetChanged();
    }
}
