package smarthome.smarthome_client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;
import smarthome.smarthome_client.clicklisteners.itemlistsuggestion.Suggestion_ItemClickListener;
import smarthome.smarthome_client.clicklisteners.itemlistsuggestion.Suggestionlist_ItemRemove_ButtonClickListener;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Suggestion_Adapter extends BaseAdapter implements Filterable
{
    private LayoutInflater mInflater;
    private ArrayList<String> mFilteredList;
    private ArrayList<String> mFullList;
    private Comparator<String> mComparator;
    private AutoCompleteTextView mACTV;
    private ItemlistActivity mActivity;
    private SuggestionFilter mFilter;


    public Suggestion_Adapter(@NonNull Context context, @NonNull ArrayList<String> suggestions, Comparator<String> comparator, AutoCompleteTextView ACTV, ItemlistActivity activity)
    {
        super();
        mInflater = LayoutInflater.from(context);
        mFilteredList = suggestions;
        mComparator = comparator;
        mACTV = ACTV;
        mActivity = activity;
    }


    public void add(@Nullable String object)
    {
        if (mFullList != null)
        {
            mFullList.add(object);
            Collections.sort(mFullList, mComparator);
        }
        else
        {
            mFilteredList.add(object);
            Collections.sort(mFilteredList, mComparator);
        }
        notifyDataSetChanged();
    }


    public void remove(@Nullable String object)
    {
        if (mFullList != null)
        {
            mFullList.remove(object);
        }
        else
        {
            mFilteredList.remove(object);
        }
        notifyDataSetChanged();
    }


    public void clear()
    {
        if (mFullList != null)
        {
            mFullList.clear();
        }
        else
        {
            mFilteredList.clear();
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount()
    {
        return mFilteredList.size();
    }


    @Override
    public String getItem(int position)
    {
        return mFilteredList.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }


    public boolean contains(String object)
    {
        return mFullList == null ? mFilteredList.contains(object) : mFullList.contains(object);
    }

    public ArrayList<String> getList()
    {
        return mFullList == null ? mFilteredList : mFullList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.custom_suggestion_row, parent, false);
        }

        TextView searchbarItemName = (TextView) convertView.findViewById(R.id.searchbar_item_name_textview);

        searchbarItemName.setOnClickListener(new Suggestion_ItemClickListener(mACTV, mActivity));
        convertView.findViewById(R.id.searchbar_item_remove_btn).setOnClickListener(new Suggestionlist_ItemRemove_ButtonClickListener(this, mACTV));

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


    @Override
    public
    @NonNull
    Filter getFilter()
    {
        if (mFilter == null)
        {
            mFilter = new SuggestionFilter();
        }
        return mFilter;
    }


    private class SuggestionFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            if (mFullList == null)
            {
                mFullList = new ArrayList<>(mFilteredList);
            }

            if (constraint == null || constraint.length() == 0)
            {
                ArrayList<String> list = new ArrayList<>(mFullList);
                results.values = list;
                results.count = list.size();
            }
            else
            {
                String constraintString = constraint.toString().toLowerCase();
                ArrayList<String> values = new ArrayList<>(mFullList);

                int count = values.size();
                ArrayList<String> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++)
                {
                    String value = values.get(i);
                    String valueString = value.toLowerCase();

                    if (valueString.startsWith(constraintString))
                    {
                        newValues.add(value);
                    }
                    else
                    {
                        String[] words = valueString.split(" ");
                        for (String word : words)
                        {
                            if (word.startsWith(constraintString))
                            {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = count;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            mFilteredList = (ArrayList<String>) results.values;
            if (results.count > 0)
            {
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }
        }
    }
}
