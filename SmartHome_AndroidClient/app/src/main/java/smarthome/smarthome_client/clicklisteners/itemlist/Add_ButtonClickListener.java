package smarthome.smarthome_client.clicklisteners.itemlist;

import android.view.View;
import android.view.View.OnClickListener;

import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Add_ButtonClickListener implements OnClickListener
{
    private ItemlistActivity mActivity;

    public Add_ButtonClickListener(ItemlistActivity activity)
    {
        this.mActivity = activity;
    }

    @Override
    public void onClick(View view)
    {
        mActivity.onAddItem(view);
    }
}
