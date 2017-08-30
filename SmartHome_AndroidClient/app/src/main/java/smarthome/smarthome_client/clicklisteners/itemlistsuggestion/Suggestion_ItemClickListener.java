package smarthome.smarthome_client.clicklisteners.itemlistsuggestion;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Suggestion_ItemClickListener implements TextView.OnClickListener
{
    private AutoCompleteTextView mItemlistSuggestionField_acTextView;
    private ItemlistActivity mActivity;

    public Suggestion_ItemClickListener(AutoCompleteTextView itemlistSuggestionField_acTextView, ItemlistActivity activity)
    {
        this.mItemlistSuggestionField_acTextView = itemlistSuggestionField_acTextView;
        this.mActivity = activity;
    }

    @Override
    public void onClick(View view)
    {
        TextView item = (TextView) view.findViewById(R.id.searchbar_item_name_textview);

        String itemName = item.getText().toString();
        mItemlistSuggestionField_acTextView.setText(itemName);
        mActivity.onAddItem(view);
    }
}
