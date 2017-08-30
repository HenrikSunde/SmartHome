package smarthome.smarthome_client.clicklisteners.navigationdrawer;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_NavigationDrawerItem_ClickListener implements ListView.OnItemClickListener
{
    private ItemlistActivity mActivity;

    public Itemlist_NavigationDrawerItem_ClickListener(ItemlistActivity activity)
    {
        mActivity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        mActivity.onDrawerItemClick(position);
    }
}
