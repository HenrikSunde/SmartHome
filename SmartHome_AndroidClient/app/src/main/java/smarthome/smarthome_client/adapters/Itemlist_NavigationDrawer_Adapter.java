package smarthome.smarthome_client.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.activity.application.itemlist.EditListActivity;
import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;
import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.models.ItemlistTitleItem;
import smarthome.smarthome_client.models.interfaces.NameableItem;

import static smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity.EDITLISTACTIVITY_REQUESTCODE;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_NavigationDrawer_Adapter extends SmartHomeBaseAdapter<ItemlistTitleItem>
{
    private Activity mActivity;
    private LayoutInflater mInflater;

    public Itemlist_NavigationDrawer_Adapter(Activity activity, ItemArraylist<ItemlistTitleItem> list, Comparator<ItemlistTitleItem> sortListWith_Comparator)
    {
        super(list, sortListWith_Comparator);
        mActivity = activity;
        mInflater = LayoutInflater.from(activity.getApplicationContext());
    }

    public void editItem(ItemlistTitleItem originalItem, ItemlistTitleItem newItem)
    {
        originalItem.setName(newItem.getName());
        originalItem.setIconResourceID(newItem.getIconResourceID());
        originalItem.setPublicList(newItem.isPublicList());
        originalItem.setSuggestions(newItem.isSuggestions());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.custom_navigationdrawer_row, parent, false);
        }

        TextView listName_TextView = (TextView) convertView.findViewById(R.id.navigation_drawer_list_item_name);
        TextView listCount_TextView = (TextView) convertView.findViewById(R.id.navigation_drawer_list_count);
        ImageView listIcon = (ImageView) convertView.findViewById(R.id.drawer_list_item_icon);
        ImageView editIcon = (ImageView) convertView.findViewById(R.id.navigation_drawer_list_item_edit_btn);

        final ItemlistTitleItem item = getItem(position);

        listName_TextView.setText(item.getName());
        listCount_TextView.setText("(" + item.getCount() + ")");
        listIcon.setBackground(convertView.getResources().getDrawable(item.getIconResourceID(), null));

        editIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editList_intent = new Intent(mActivity.getApplicationContext(), EditListActivity.class);
                editList_intent.putExtra("edit", true);
                editList_intent.putExtra("itemlistName", item.getName());
                editList_intent.putExtra("icon", item.getIconResourceID());
                editList_intent.putExtra("listPublic", item.isPublicList());
                mActivity.startActivityForResult(editList_intent, EDITLISTACTIVITY_REQUESTCODE);
            }
        });

        return convertView;
    }
}
