package smarthome.smarthome_client.listeners;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import smarthome.smarthome_client.activity.application.shoppinglist.ShoppinglistActivity;
import static smarthome.smarthome_client.logic.Logic.*;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Searchbar_TextWatcher implements TextWatcher
{
    private ShoppinglistActivity activity;
    private AutoCompleteTextView view;

    private boolean backspace = false;

    public Searchbar_TextWatcher(ShoppinglistActivity activity, AutoCompleteTextView view)
    {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        backspace = count > after;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String text = s.toString();
        
        if (text.contains(System.getProperty("line.separator")) && text.trim().replace(System.getProperty("line.separator"), "").equals(""))
        {
            s.clear();
            return;
        }

        // If the first letter is lowercase - make it uppercase
        if (!backspace && charIsLowerCase(text.charAt(0)))
        {
            s.replace(0, 1, String.valueOf(text.charAt(0)).toUpperCase());
        }

        // If the text contains a newline character the text should be submitted as a new item
        if (text.contains(System.getProperty("line.separator")))
        {
            activity.onAddItem(view);
        }
    }
}
