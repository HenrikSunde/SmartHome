package smarthome.smarthome_client.activity.application.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.adapters.SearchbarAdapter;
import smarthome.smarthome_client.adapters.ShoppinglistAdapter;
import smarthome.smarthome_client.arraylists.ShoppinglistItemArrayList;
import smarthome.smarthome_client.comparators.ItemSuggestionComparator;
import smarthome.smarthome_client.comparators.ShoppinglistItemComparator;
import smarthome.smarthome_client.database.DatabaseContract;
import smarthome.smarthome_client.database.DatabaseContract.DatabaseEntry;
import smarthome.smarthome_client.database.DbHelper;
import smarthome.smarthome_client.listeners.Searchbar_TextWatcher;
import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppinglistActivity extends Activity
{
    // Constants
    private static final String LOGTAG = "ShoppinglistActivity";

    // Views
    private AutoCompleteTextView mShoppinglistItemSearchbar_acTextView;
    private ListView mShoppinglist_listView;

    // Searchbar
    private ArrayList<String> mItemSuggestions;
    private SearchbarAdapter mSearchbarAdapter;
    private Comparator<ShoppingListItem> mShoppingListItemComparator;

    // Shoppinglist
    private ShoppinglistItemArrayList mShoppinglist;
    private ShoppinglistAdapter mShoppinglistAdapter;
    private Comparator<String> mItemSuggestionComparator;

    // Database
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;


    /**
     * ON CREATE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);

        Log.i(LOGTAG, "*** Start of onCreate() ***");

        // Instantiate the database helper
        mDbHelper = new DbHelper(getApplicationContext());

        // Get the searchbar and populate the hint with the array of all shoppinglist items
        mItemSuggestions = new ArrayList<>();
        mSearchbarAdapter = new SearchbarAdapter(this, mItemSuggestions);
        mShoppinglistItemSearchbar_acTextView = (AutoCompleteTextView) findViewById(R.id.item_searchbar_textView);
        mShoppinglistItemSearchbar_acTextView.setAdapter(mSearchbarAdapter);
        mShoppinglistItemSearchbar_acTextView.setThreshold(1);
        mShoppinglistItemSearchbar_acTextView.addTextChangedListener(new Searchbar_TextWatcher(this, mShoppinglistItemSearchbar_acTextView));
        mItemSuggestionComparator = ItemSuggestionComparator.getNameComparator(true);

        // Get the listview for the current shoppinglist and populate it with the shoppinglist array
        mShoppinglist = new ShoppinglistItemArrayList();
        mShoppinglistAdapter = new ShoppinglistAdapter(this, mShoppinglist);
        mShoppinglist_listView = (ListView) findViewById(R.id.shoppinglist_listview);
        mShoppinglist_listView.setAdapter(mShoppinglistAdapter);
        mShoppingListItemComparator = ShoppinglistItemComparator.getDateComparator(false);
    }

    /**
     * ON START
     */
    @Override
    protected void onStart()
    {
        super.onStart();

        Log.i(LOGTAG, "*** Start of onStart() ***");
    }

    /**
     * ON RESTART
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.i(LOGTAG, "*** Start of onRestart() ***");
    }

    /**
     * ON RESUME
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        Log.i(LOGTAG, "*** Start of onResume() ***");

        // TODO Read 'shoppinglist' values from database
        // also, retrieve any updates from the SmartHome Application Server (SHAS)
        // both these things should happen in the background

        mDb = mDbHelper.getReadableDatabase();

        // Read all data from the database
        ArrayList<String> itemSuggestionsFromDb = DbHelper.getAllSuggestionEntries(mDb);
        // All data is read, the table can be cleared and is then ready for saving new items
        DbHelper.clearAllEntries(mDb, DatabaseEntry.TABLE_ITEMSUGGESTIONS);
        mItemSuggestions.clear();

        // Read all data from the database
        ShoppinglistItemArrayList shoppinglistFromDb = DbHelper.getAllShoppinglistItemEntries(mDb, DatabaseEntry.TABLE_DEFAULTSHOPPINGLIST, null);
        // All data is read, the table can be cleared and is then ready for saving new items
        DbHelper.clearAllEntries(mDb, DatabaseEntry.TABLE_DEFAULTSHOPPINGLIST);
        mShoppinglistAdapter.clear();


        mDb.close();

        if (itemSuggestionsFromDb.size() > 0)
        {
            Log.i(LOGTAG, "Number of suggestions read from db: " + itemSuggestionsFromDb.size());
            for (String  itemName : itemSuggestionsFromDb)
            {
                mItemSuggestions.add(itemName);
            }
            Collections.sort(mItemSuggestions, mItemSuggestionComparator);
            mSearchbarAdapter.notifyDataSetChanged();
        }

        if (shoppinglistFromDb.size() > 0)
        {
            Log.i(LOGTAG, "Number of shoppinglistitems read from db: " + shoppinglistFromDb.size());
            for (ShoppingListItem  item : shoppinglistFromDb)
            {
                mShoppinglistAdapter.add(item);
            }
            Collections.sort(mShoppinglist, mShoppingListItemComparator);
            mShoppinglistAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ON PAUSE
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        Log.i(LOGTAG, "*** Start of onPause() ***");

        // TODO Update the database with the values in 'shoppinglist'
        // also, send the values to the SmartHome Application Server (SHAS)
        // both these things should happen in the background

        mDb = mDbHelper.getWritableDatabase();

        DbHelper.insertAllSuggestionEntries(mDb, DatabaseEntry.TABLE_ITEMSUGGESTIONS, mItemSuggestions);
        Log.i(LOGTAG, "Number of suggestions inserted into db: " + mItemSuggestions.size());

        DbHelper.insertAllShoppinglistItemEntries(mDb, DatabaseEntry.TABLE_DEFAULTSHOPPINGLIST, mShoppinglist);
        Log.i(LOGTAG, "Number of shoppinglistitems inserted into db: " + mShoppinglist.size());

        mDb.close();
    }

    /**
     * ON STOP
     */
    @Override
    protected void onStop()
    {
        super.onStop();

        Log.i(LOGTAG, "*** Start of onStop() ***");
    }

    /**
     * ON DESTROY
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.i(LOGTAG, "*** Start of onDestroy() ***");

        mDbHelper.close();
    }

    /**
     * Add button is clicked
     */
    public void onAddItem(View view)
    {
        String itemName = mShoppinglistItemSearchbar_acTextView.getText().toString().trim().replace(System.getProperty("line.separator"), "");

        if (itemName.equals(""))
        {
            return;
        }

        if (!mItemSuggestions.contains(itemName))
        {
            // Add the item as a suggested item
            mSearchbarAdapter.add(itemName);
            Collections.sort(mItemSuggestions, mItemSuggestionComparator);

            for (String tItemName : mItemSuggestions)
            {
                Log.i("mItemSuggestions", tItemName);
            }

            mSearchbarAdapter.notifyDataSetChanged();
        }

        if (mShoppinglistAdapter.contains(itemName))
        {
            // The item already exists in the list
            Toast.makeText(view.getContext(), itemName + " is already added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // The item does not exist in the list, it should be added
            mShoppinglistAdapter.add(new ShoppingListItem(itemName));
            Collections.sort(mShoppinglist, mShoppingListItemComparator);

            for (ShoppingListItem item : mShoppinglist)
            {
                Log.i("mShoppinglist", item.getItemName());
            }

            mShoppinglistAdapter.notifyDataSetChanged();
        }

        // Clear the searchbar
        mShoppinglistItemSearchbar_acTextView.setText("");
    }

    /**
     * Shoppinglist list-item is clicked - should mark or unmark the item
     */
    public void onShoppinglistItemClick(View view)
    {
        TextView item_textView = (TextView) view.findViewById(R.id.shoppinglist_item_name_textview);

        String itemName = item_textView.getText().toString();
        ShoppingListItem item = mShoppinglist.getItem(itemName);

        // Switch between marked and unmarked
        item.setMarked(!item.isMarked());

        mShoppinglistAdapter.notifyDataSetChanged();
    }

    /**
     * The X on a shoppinglist list-item is clicked
     */
    public void onShoppinglistItemRemoveBtnClick(View view)
    {
        ViewGroup parent = (ViewGroup) view.getParent();
        TextView item_textView = (TextView) parent.findViewById(R.id.shoppinglist_item_name_textview);

        String itemName = item_textView.getText().toString();
        mShoppinglist.removeItem(itemName);
        mShoppinglistAdapter.notifyDataSetChanged();
    }

    /**
     * Searchbar list-item is clicked - should add the item to the shoppinglist
     */
    public void onSearchbarItemClick(View view)
    {
        TextView item = (TextView) view.findViewById(R.id.searchbar_item_name_textview);

        String itemName = item.getText().toString();
        mShoppinglistItemSearchbar_acTextView.setText(itemName);
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
        mSearchbarAdapter.remove(itemName);
        mSearchbarAdapter.notifyDataSetChanged();

        // The TextView have to be updated for the adapter to work as intended. Don't know why, but it works.
        // Set the cursor position after the TextView is refreshed
        mShoppinglistItemSearchbar_acTextView.setText(mShoppinglistItemSearchbar_acTextView.getText());
        mShoppinglistItemSearchbar_acTextView.setSelection(mShoppinglistItemSearchbar_acTextView.getText().length());
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
                    mShoppinglistAdapter.clear();
                    mShoppinglistAdapter.notifyDataSetChanged();
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
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    // Make an ArrayList with the items that should be removed (marked)
                    ArrayList<String> removeTheseItems = new ArrayList<>();
                    for (ShoppingListItem item : mShoppinglist)
                    {
                        if (item.isMarked())
                        {
                            removeTheseItems.add(item.getItemName());
                        }
                    }

                    // Remove the marked items
                    for (String itemName : removeTheseItems)
                    {
                        mShoppinglist.removeItem(itemName);
                    }

                    if (!removeTheseItems.isEmpty())
                    {
                        mShoppinglistAdapter.notifyDataSetChanged();
                    }
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
        imm.hideSoftInputFromWindow(mShoppinglistItemSearchbar_acTextView.getWindowToken(), 0);
    }
}
