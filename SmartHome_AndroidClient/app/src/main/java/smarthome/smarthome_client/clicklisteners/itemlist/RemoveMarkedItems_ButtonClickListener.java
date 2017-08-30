package smarthome.smarthome_client.clicklisteners.itemlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import smarthome.smarthome_client.adapters.Itemlist_Adapter;
import smarthome.smarthome_client.arraylists.ItemlistItem_ArrayList;
import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class RemoveMarkedItems_ButtonClickListener implements Button.OnClickListener
{
    private ItemlistItem_ArrayList mItemlist;
    private Itemlist_Adapter mItemlistAdapter;

    public RemoveMarkedItems_ButtonClickListener(ItemlistItem_ArrayList itemlist, Itemlist_Adapter itemlistAdapter)
    {
        this.mItemlist = itemlist;
        this.mItemlistAdapter = itemlistAdapter;
    }

    @Override
    public void onClick(View view)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    // Make an ArrayList with the items that should be removed (marked)
                    ArrayList<String> removeTheseItems = new ArrayList<>();
                    for (ItemlistItem item : mItemlist)
                    {
                        if (item.isMarked())
                        {
                            removeTheseItems.add(item.getItemName());
                        }
                    }

                    // Remove the marked items
                    for (String itemName : removeTheseItems)
                    {
                        mItemlistAdapter.remove(itemName);
                    }

                    if (!removeTheseItems.isEmpty())
                    {
                        mItemlistAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        if (!mItemlist.isEmpty())
        {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
            alertBuilder.setMessage("Remove all marked items from the list?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }
    }
}
