package smarthome.smarthome_client.clicklisteners.itemlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.adapters.Itemlist_Adapter;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_ItemRemove_ButtonClickListener implements Button.OnClickListener
{
    private Itemlist_Adapter mItemlistAdapter;

    public Itemlist_ItemRemove_ButtonClickListener(Itemlist_Adapter itemlistAdapter)
    {
        this.mItemlistAdapter = itemlistAdapter;
    }

    @Override
    public void onClick(View view)
    {
        ViewGroup parent = (ViewGroup) view.getParent();
        TextView item_textView = (TextView) parent.findViewById(R.id.shoppinglist_item_name_textview);

        String itemName = item_textView.getText().toString();
        mItemlistAdapter.remove(itemName);
        mItemlistAdapter.notifyDataSetChanged();
    }
}
