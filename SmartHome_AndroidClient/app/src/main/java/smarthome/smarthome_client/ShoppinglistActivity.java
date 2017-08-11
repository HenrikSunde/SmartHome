package smarthome.smarthome_client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import smarthome.smarthome_client.adapters.SearchbarAdapter;
import smarthome.smarthome_client.adapters.ShoppinglistAdapter;
import smarthome.smarthome_client.arraylists.ItemArrayList;
import smarthome.smarthome_client.database.DatabaseContract.DatabaseEntry;
import smarthome.smarthome_client.database.DatabaseDbHelper;
import smarthome.smarthome_client.listeners.Searchbar_TextWatcher;
import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppinglistActivity extends Activity
{
    private static final String LOGTAG = "ShoppinglistActivity";

    private AutoCompleteTextView shoppinglistItemSearchbar_acTextView;
    private ListView shoppinglist_listView;
    private ItemArrayList shoppinglist = new ItemArrayList();
    private ArrayList<String> shoppinglistItemSuggestions = new ArrayList<>();
    private ArrayAdapter searchbarAdapter;
    private ArrayAdapter shoppinglistAdapter;

    private DatabaseDbHelper mDbHelper;
    private SQLiteDatabase db;

    /**
     * ON CREATE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(LOGTAG, "*** Start of onCreate() ***");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);

        // Instantiate the database helper
        mDbHelper = DatabaseDbHelper.getDbHelper(getApplicationContext());

        // Get the searchbar and populate the hint with the array of all shoppinglist items
        searchbarAdapter = new SearchbarAdapter<>(this, shoppinglistItemSuggestions);
        shoppinglistItemSearchbar_acTextView = (AutoCompleteTextView) findViewById(R.id.item_searchbar_textView);
        shoppinglistItemSearchbar_acTextView.setAdapter(searchbarAdapter);
        shoppinglistItemSearchbar_acTextView.setThreshold(1);
        shoppinglistItemSearchbar_acTextView.addTextChangedListener(new Searchbar_TextWatcher(this, shoppinglistItemSearchbar_acTextView));

        // Get the listview for the current shoppinglist and populate it with the shoppinglist array
        shoppinglistAdapter = new ShoppinglistAdapter(this, shoppinglist);
        shoppinglist_listView = (ListView) findViewById(R.id.shoppinglist_listview);
        shoppinglist_listView.setAdapter(shoppinglistAdapter);
    }

    /**
     * ON START
     */
    @Override
    protected void onStart()
    {
        Log.i(LOGTAG, "*** Start of onStart() ***");

        super.onStart();
    }

    /**
     * ON RESTART
     */
    @Override
    protected void onRestart()
    {
        Log.i(LOGTAG, "*** Start of onRestart() ***");

        super.onRestart();
    }

    /**
     * ON RESUME
     */
    @Override
    protected void onResume()
    {
        Log.i(LOGTAG, "*** Start of onResume() ***");

        // TODO Read 'shoppinglist' values from database
        // also, retrieve any updates from the SmartHome Application Server (SHAS)
        // both these things should happen in the background
        db = mDbHelper.getReadableDatabase();

        // Select all items from table
        Cursor cursor = db.query(
                DatabaseEntry.TABLE_SHOPPINGLISTITEM,
                null,
                null,
                null,
                null,
                null,
                DatabaseEntry.COLUMN_ITEMNAME + " DESC");

        while (cursor.moveToNext())
        {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMNAME));
            boolean itemMarked = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseEntry.COLUMN_ITEMMARKED)) == 1;

            shoppinglist.add(new ShoppingListItem(itemName, itemMarked));
        }
        cursor.close();

        // All data is read, the table can be cleared and is then ready for saving new items
        String clearTable = "DELETE FROM " + DatabaseEntry.TABLE_SHOPPINGLISTITEM;
        db.execSQL(clearTable);
        db.close();

        shoppinglistAdapter.notifyDataSetChanged();

        super.onResume();
    }

    /**
     * ON PAUSE
     */
    @Override
    protected void onPause()
    {
        Log.i(LOGTAG, "*** Start of onPause() ***");

        // TODO Update the database with the values in 'shoppinglist'
        // also, send the values to the SmartHome Application Server (SHAS)
        // both these things should happen in the background
        db = mDbHelper.getWritableDatabase();

        ContentValues values;
        for (Object obj : shoppinglist)
        {
            ShoppingListItem item = (ShoppingListItem) obj;
            values = new ContentValues();
            values.put(DatabaseEntry.COLUMN_ITEMNAME, item.getItemName());
            values.put(DatabaseEntry.COLUMN_ITEMMARKED, item.isMarked() ? 1 : 0);
            long newRowId = db.insert(DatabaseEntry.TABLE_SHOPPINGLISTITEM, null, values);
        }

        db.close();

        super.onPause();
    }

    /**
     * ON STOP
     */
    @Override
    protected void onStop()
    {
        Log.i(LOGTAG, "*** Start of onStop() ***");

        super.onStop();
    }

    /**
     * ON DESTROY
     */
    @Override
    protected void onDestroy()
    {
        Log.i(LOGTAG, "*** Start of onDestroy() ***");

        mDbHelper.close();

        super.onDestroy();
    }

    /**
     * Add button is clicked
     */
    public void onAddItem(View view)
    {
        String itemName = shoppinglistItemSearchbar_acTextView.getText().toString().trim().replace(System.getProperty("line.separator"), "");

        if (itemName.equals(""))
        {
            return;
        }

        if (shoppinglist.contains(itemName))
        {
            // The item already exists in the list
            Toast.makeText(view.getContext(), itemName + " is already added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // The item does not exist in the list, it should be added
            shoppinglist.add(new ShoppingListItem(itemName));
            shoppinglistAdapter.notifyDataSetChanged();
        }

        if (!shoppinglistItemSuggestions.contains(itemName))
        {
            // Add the item as a suggested item
            shoppinglistItemSuggestions.add(itemName);

            // TODO Change not noticed
            searchbarAdapter.notifyDataSetChanged();
        }

        // Clear the searchbar
        shoppinglistItemSearchbar_acTextView.setText("");
    }

    /**
     * Shoppinglist list-item is clicked - should mark or unmark the item
     */
    public void onShoppinglistItemClick(View view)
    {
        TextView item_textView = (TextView) view.findViewById(R.id.shoppinglist_item_name_textview);

        String itemName = item_textView.getText().toString();
        ShoppingListItem item = (ShoppingListItem) shoppinglist.getItem(itemName);

        // Switch between marked and unmarked
        item.setMarked(!item.isMarked());

        shoppinglistAdapter.notifyDataSetChanged();
    }

    /**
     * The X on a shoppinglist list-item is clicked
     */
    public void onShoppinglistItemRemoveBtnClick(View view)
    {
        ViewGroup parent = (ViewGroup) view.getParent();
        TextView item_textView = (TextView) parent.findViewById(R.id.shoppinglist_item_name_textview);

        String itemName = item_textView.getText().toString();
        shoppinglist.removeItem(itemName);

        shoppinglistAdapter.notifyDataSetChanged();
    }

    /**
     * Searchbar list-item is clicked - should add the item to the shoppinglist
     */
    public void onSearchbarItemClick(View view)
    {
        TextView item = (TextView) view.findViewById(R.id.searchbar_item_name_textview);

        String itemName = item.getText().toString();
        shoppinglistItemSearchbar_acTextView.setText(itemName);
        onAddItem(view);
    }

    /**
     * The X on a searchbar list-item is clicked
     */
    public void onSearchbarlistItemRemoveBtnClick(View view)
    {
        ViewGroup parent = (ViewGroup) view.getParent();
        TextView item_textView = (TextView) parent.findViewById(R.id.searchbar_item_name_textview);

        String itemName = item_textView.getText().toString();
        shoppinglistItemSuggestions.remove(itemName);

        // TODO Change not noticed
        searchbarAdapter.notifyDataSetChanged();

        shoppinglistItemSearchbar_acTextView.setSelection(shoppinglistItemSearchbar_acTextView.getText().length());
    }

    /**
     * Completely clear the list
     */
    public void onRemoveAllItemsBtnClick(View view)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    shoppinglist.clear();
                    shoppinglistAdapter.notifyDataSetChanged();
                }
            }
        };
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
        alertBuilder.setMessage("Clear the shoppinglist?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    /**
     * Clear every crossed item from the list
     */
    public void onRemoveMarkedItemsBtnClick(View view)
    {
        // TODO Clean code
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    ArrayList<ShoppingListItem> removeTheseItems = new ArrayList<>();
                    for (Object object : shoppinglist)
                    {
                        ShoppingListItem item = (ShoppingListItem) object;
                        if (item.isMarked())
                        {
                            removeTheseItems.add(item);
                        }
                    }
                    for (ShoppingListItem item : removeTheseItems)
                    {
                        shoppinglist.remove(item);
                    }
                    removeTheseItems.clear();
                    shoppinglistAdapter.notifyDataSetChanged();
                }
            }
        };
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
        alertBuilder.setMessage("Remove all marked items from the shoppinglist?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    /**
     * Hide keyboard button is clicked
     */
    public void onHideKeyboardBtnClick(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(shoppinglistItemSearchbar_acTextView.getWindowToken(), 0);
    }
}
