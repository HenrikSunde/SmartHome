package smarthome.smarthome_client.clicklisteners.itemlist;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class HideKeyboard_ButtonClickListener implements Button.OnClickListener
{
    private Context mContext;
    private AutoCompleteTextView mACTV;

    public HideKeyboard_ButtonClickListener(Context context, AutoCompleteTextView ACTV)
    {
        this.mContext = context;
        this.mACTV = ACTV;
    }

    @Override
    public void onClick(View v)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mACTV.getWindowToken(), 0);
    }
}
