package smarthome.smarthome_client.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

import static smarthome.smarthome_client.logic.Logic.charIsLowerCase;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class SuggestionField_TextWatcher implements TextWatcher
{
    private final static String newLine = System.getProperty("line.separator");

    private ItemlistActivity activity;
    private AutoCompleteTextView view;

    private boolean backspace = false;


    public SuggestionField_TextWatcher(ItemlistActivity activity, AutoCompleteTextView view)
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

        // If the text contains only spaces and newlines, clear the text. This is a workaround for an annoyance
        if (text.contains(newLine) && text.trim().replace(newLine, "").equals(""))
        {
            s.clear();
            return;
        }

        // If the first letter is lowercase - make it uppercase
        if (text.trim().length() > 0)
        {
            char firstNonSpaceChar = text.trim().charAt(0);
            int indexOfFirstNonSpaceChar = text.indexOf(firstNonSpaceChar);
            if (!backspace && charIsLowerCase(text.charAt(indexOfFirstNonSpaceChar)))
            {
                s.replace(indexOfFirstNonSpaceChar, indexOfFirstNonSpaceChar + 1, String.valueOf(firstNonSpaceChar).toUpperCase());
            }
        }

        // If the text contains a newline character the text should be submitted as a new item
        if (text.contains(newLine))
        {
            activity.onAddItem(view);
        }
    }
}
