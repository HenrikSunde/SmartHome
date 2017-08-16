package smarthome.smarthome_client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.comparators.ItemSuggestionComparator;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class SearchbarAdapter extends ArrayAdapter<String>
{
    private LayoutInflater mInflater;
    private ArrayList<String> mList;

    public SearchbarAdapter(@NonNull Context context, @NonNull ArrayList<String> suggestions)
    {
        super(context, 0, suggestions);
        mInflater = LayoutInflater.from(context);
        mList = suggestions;
    }

    @Override
    public void add(@Nullable String object)
    {
        super.add(object);
        super.sort(ItemSuggestionComparator.getNameComparator(true));
        mList.add(object);
    }

    @Override
    public void remove(@Nullable String object)
    {
        super.remove(object);
        mList.remove(object);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.custom_searchbar_row, parent, false);
        }

        TextView searchbarItemName = (TextView) convertView.findViewById(R.id.searchbar_item_name_textview);

        String singleItem = getItem(position);
        if (singleItem != null)
        {
            searchbarItemName.setText(singleItem);
        }
        else
        {
            searchbarItemName.setText("N/A");
        }

        return convertView;
    }
}
