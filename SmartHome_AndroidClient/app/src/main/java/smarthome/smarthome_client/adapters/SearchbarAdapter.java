package smarthome.smarthome_client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import smarthome.smarthome_client.R;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class SearchbarAdapter<String> extends ArrayAdapter<String>
{
    public SearchbarAdapter(@NonNull Context context, @NonNull List<String> objects)
    {
        super(context, R.layout.custom_searchbar_row, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_searchbar_row, parent, false);

        String singleItem = getItem(position);
        TextView searchbarItemName = (TextView) customView.findViewById(R.id.searchbar_item_name_textview);

        searchbarItemName.setText((CharSequence) singleItem);

        return customView;
    }
}
