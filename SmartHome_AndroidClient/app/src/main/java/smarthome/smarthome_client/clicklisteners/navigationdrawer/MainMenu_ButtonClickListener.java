package smarthome.smarthome_client.clicklisteners.navigationdrawer;

import android.view.View;
import android.widget.Button;

import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class MainMenu_ButtonClickListener implements Button.OnClickListener
{
    private ItemlistActivity mActivity;

    public MainMenu_ButtonClickListener(ItemlistActivity activity)
    {
        this.mActivity = activity;
    }

    @Override
    public void onClick(View view)
    {
        mActivity.finish();
    }
}
