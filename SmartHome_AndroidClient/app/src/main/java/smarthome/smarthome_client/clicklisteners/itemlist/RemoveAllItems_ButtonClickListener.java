package smarthome.smarthome_client.clicklisteners.itemlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import smarthome.smarthome_client.adapters.Itemlist_Adapter;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class RemoveAllItems_ButtonClickListener implements Button.OnClickListener
{
    private Itemlist_Adapter mItemlistAdapter;

    public RemoveAllItems_ButtonClickListener(Itemlist_Adapter itemlistAdapter)
    {
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
                    mItemlistAdapter.clear();
                    mItemlistAdapter.notifyDataSetChanged();
                }
            }
        };
        if (!mItemlistAdapter.isEmpty())
        {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
            alertBuilder.setMessage("Clear the list?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }
    }
}
