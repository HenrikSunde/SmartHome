package smarthome.smarthome_client.clicklisteners.itemlistsuggestion;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.adapters.Suggestion_Adapter;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Suggestionlist_ItemRemove_ButtonClickListener implements Button.OnClickListener
{
    private Suggestion_Adapter mSuggestionAdapter;
    private AutoCompleteTextView mItemlistSuggestionField_acTextView;

    public Suggestionlist_ItemRemove_ButtonClickListener(Suggestion_Adapter suggestionAdapter, AutoCompleteTextView itemlistSuggestionField_acTextView)
    {
        this.mSuggestionAdapter = suggestionAdapter;
        this.mItemlistSuggestionField_acTextView = itemlistSuggestionField_acTextView;
    }

    @Override
    public void onClick(View view)
    {
        ViewGroup parent = (ViewGroup) view.getParent();
        TextView item_textView = (TextView) parent.findViewById(R.id.searchbar_item_name_textview);

        String itemName = item_textView.getText().toString();
        mSuggestionAdapter.remove(itemName);
        mSuggestionAdapter.notifyDataSetChanged();

        // The TextView have to be updated for the adapter to work as intended. Don't know why, but it works.
        // Set the cursor position after the TextView is refreshed
        mItemlistSuggestionField_acTextView.setText(mItemlistSuggestionField_acTextView.getText());
        mItemlistSuggestionField_acTextView.setSelection(mItemlistSuggestionField_acTextView.getText().length());
    }
}
