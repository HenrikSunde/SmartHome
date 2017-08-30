package smarthome.smarthome_client.adapters;

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
import smarthome.smarthome_client.arraylists.NavigationDrawerItem_ArrayList;
import smarthome.smarthome_client.models.NavigationDrawerItem;

import static smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity.EDITLISTACTIVITY_REQUESTCODE;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_NavigationDrawer_Adapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private NavigationDrawerItem_ArrayList mList;
    private Comparator<NavigationDrawerItem> mComparator;
    private ItemlistActivity mActivity;

    public Itemlist_NavigationDrawer_Adapter(@NonNull Context context, @NonNull NavigationDrawerItem_ArrayList list, Comparator<NavigationDrawerItem> comparator, ItemlistActivity activity)
    {
        super();
        mInflater = LayoutInflater.from(context);
        mList = list;
        mComparator = comparator;
        mActivity = activity;
    }


    public void add(NavigationDrawerItem item)
    {
        mList.add(item);
        Collections.sort(mList, mComparator);
        notifyDataSetChanged();
    }


    public void remove(NavigationDrawerItem item)
    {
        mList.remove(item);
        notifyDataSetChanged();
    }


    public void remove(String itemName)
    {
        mList.removeItem(itemName);
        notifyDataSetChanged();
    }

    public boolean contains(String itemName)
    {
        return mList.contains(itemName);
    }

    public NavigationDrawerItem getItem(String itemName)
    {
        return mList.getItem(itemName);
    }

    public void editItem(String itemName, NavigationDrawerItem newItem)
    {
        remove(itemName);
        add(newItem);
    }

    public void clear()
    {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }


    @Override
    public NavigationDrawerItem getItem(int position)
    {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.custom_navigationdrawer_row, parent, false);
        }
        TextView listName_TextView = (TextView) convertView.findViewById(R.id.navigation_drawer_list_item_name);
        ImageView listIcon = (ImageView) convertView.findViewById(R.id.drawer_list_item_icon);
        ImageView editIcon = (ImageView) convertView.findViewById(R.id.navigation_drawer_list_item_edit_btn);

        final NavigationDrawerItem item = getItem(position);

        listName_TextView.setText(item.getItemName());
        listIcon.setBackground(convertView.getResources().getDrawable(item.getIconResourceID(), null));

        editIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editList_intent = new Intent(mActivity.getApplicationContext(), EditListActivity.class);
                editList_intent.putExtra("edit", true);
                editList_intent.putExtra("itemlistName", item.getItemName());
                editList_intent.putExtra("icon", item.getIconResourceID());
                editList_intent.putExtra("listPublic", item.isPublicList());
                mActivity.startActivityForResult(editList_intent, EDITLISTACTIVITY_REQUESTCODE);
            }
        });

        return convertView;
    }
}
