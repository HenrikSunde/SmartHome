package smarthome.smarthome_client.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;
import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.exceptions.NotImplementedException;
import smarthome.smarthome_client.models.Suggestion;
import smarthome.smarthome_client.models.interfaces.NameableItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Suggestion_Adapter extends SmartHomeBaseAdapter<Suggestion> implements Filterable
{
    private LayoutInflater mInflater;
    private ItemArraylist<Suggestion> mFilteredList;
    private ItemArraylist<Suggestion> mFullList;
    private Comparator<Suggestion> mComparator;
    private ItemlistActivity mActivity;
    private SuggestionFilter mFilter;


    public Suggestion_Adapter(ItemlistActivity activity, ItemArraylist<Suggestion> suggestions, Comparator<Suggestion> comparator)
    {
        super(suggestions, comparator);
        mInflater = LayoutInflater.from(activity.getApplicationContext());
        mFilteredList = suggestions;
        mComparator = comparator;
        mActivity = activity;
    }


    @Override
    public void add(Suggestion item)
    {
        if (mFullList != null)
        {
            mFullList.add(item);
            Collections.sort(mFullList, mComparator);
        }
        else
        {
            mFilteredList.add(item);
            Collections.sort(mFilteredList, mComparator);
        }
    }

    @Override
    public void remove(Suggestion item)
    {
        if (mFullList != null)
        {
            mFullList.remove(item);
        }
        else
        {
            super.remove(item);
        }
    }

    @Override
    public void remove(String itemName)
    {
        if (mFullList != null)
        {
            mFullList.removeItem(itemName);
        }
        else
        {
            super.remove(itemName);
        }
    }

    @Override
    public void clear()
    {
        if (mFullList != null)
        {
            mFullList.clear();
        }
        else
        {
            super.clear();
        }
    }

    @Override
    public int getCount()
    {
        return mFullList != null ? mFullList.size() : super.getCount();
    }

    @Override
    public Suggestion getItem(String itemName)
    {
        return mFullList != null ? mFullList.getItem(itemName) : super.getItem(itemName);
    }

    @Override
    public int getPosition(String itemName)
    {
        return mFullList != null ? mFullList.getPosition(itemName) : super.getPosition(itemName);
    }

    @Override
    public int getPosition(Suggestion item)
    {
        return mFullList != null ? mFullList.getPosition(item) : super.getPosition(item);
    }

    @Override
    public Suggestion getItem(int position)
    {
        return mFullList != null ? mFullList.get(position) : super.getItem(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean contains(Suggestion item)
    {
        return mFullList != null ? mFullList.contains(item) : super.contains(item);
    }

    @Override
    public boolean contains(String itemName)
    {
        return mFullList != null ? mFullList.contains(itemName) : super.contains(itemName);
    }

    @Override
    public ItemArraylist<Suggestion> getList()
    {
        return mFullList != null ? mFullList : mFilteredList;
    }

    @Override
    public void setList(ItemArraylist<Suggestion> list)
    {
        if (mFullList != null)
        {
            mFullList.clear();
            for (Suggestion item : list)
            {
                mFullList.add(item);
            }
            Collections.sort(mFullList, mComparator);
        }
        else
        {
            super.setList(list);
        }
    }

    @Override
    public void editItem(Suggestion originalItem, Suggestion newItem)
    {
        throw new NotImplementedException();
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
        final AutoCompleteTextView ACTV = (AutoCompleteTextView) mActivity.findViewById(R.id.item_searchbar_textView);
        Button removeSuggestionButton = (Button) convertView.findViewById(R.id.searchbar_item_remove_btn);

        searchbarItemName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView item = (TextView) v.findViewById(R.id.searchbar_item_name_textview);

                String itemName = item.getText().toString();
                ACTV.setText(itemName);
                mActivity.onAddItem(v);
            }
        });
        removeSuggestionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ViewGroup parent = (ViewGroup) v.getParent();
                TextView item_textView = (TextView) parent.findViewById(R.id.searchbar_item_name_textview);

                Suggestion suggestion = getItem(item_textView.getText().toString());
                mActivity.removeSuggestion(suggestion);

                // The TextView have to be updated for the adapter to work as intended. Don't know why, but it works.
                // Set the cursor position after the TextView is refreshed
                ACTV.setText(ACTV.getText());
                ACTV.setSelection(ACTV.getText().length());
            }
        });

        Suggestion singleItem = getItem(position);
        if (singleItem != null)
        {
            searchbarItemName.setText(singleItem.getName());
        }
        else
        {
            searchbarItemName.setText("N/A");
        }

        return convertView;
    }


    @Override
    @NonNull
    public Filter getFilter()
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
                mFullList = new ItemArraylist<>(mFilteredList);
            }

            if (constraint == null || constraint.length() == 0)
            {
                ItemArraylist<Suggestion> list = new ItemArraylist<>(mFullList);
                results.values = list;
                results.count = list.size();
            }
            else
            {
                String constraintString = constraint.toString().toLowerCase();
                ItemArraylist<Suggestion> values = new ItemArraylist<>(mFullList);

                int count = values.size();
                ItemArraylist<Suggestion> newValues = new ItemArraylist<>();

                for (int i = 0; i < count; i++)
                {
                    Suggestion value = values.get(i);
                    String valueString = value.getName().toLowerCase();

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
            mFilteredList = (ItemArraylist<Suggestion>) results.values;
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
