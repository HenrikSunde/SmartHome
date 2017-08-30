package smarthome.smarthome_client.activity.application.itemlist;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.actionbardrawertoggles.Itemlist_NavigationDrawer_Toggle;
import smarthome.smarthome_client.adapters.Suggestion_Adapter;
import smarthome.smarthome_client.adapters.Itemlist_Adapter;
import smarthome.smarthome_client.adapters.Itemlist_NavigationDrawer_Adapter;
import smarthome.smarthome_client.arraylists.NavigationDrawerItem_ArrayList;
import smarthome.smarthome_client.arraylists.ItemlistItem_ArrayList;
import smarthome.smarthome_client.clicklisteners.navigationdrawer.Itemlist_NavigationDrawerItem_ClickListener;
import smarthome.smarthome_client.clicklisteners.navigationdrawer.MainMenu_ButtonClickListener;
import smarthome.smarthome_client.clicklisteners.itemlist.Add_ButtonClickListener;
import smarthome.smarthome_client.clicklisteners.itemlist.HideKeyboard_ButtonClickListener;
import smarthome.smarthome_client.clicklisteners.itemlist.RemoveAllItems_ButtonClickListener;
import smarthome.smarthome_client.clicklisteners.itemlist.RemoveMarkedItems_ButtonClickListener;
import smarthome.smarthome_client.comparators.SuggestionItem_Comparator;
import smarthome.smarthome_client.comparators.NavigationDrawerItem_Comparator;
import smarthome.smarthome_client.comparators.ItemlistItem_Comparator;
import smarthome.smarthome_client.database.DbHelper;
import smarthome.smarthome_client.listeners.SuggestionField_TextWatcher;
import smarthome.smarthome_client.models.NavigationDrawerItem;
import smarthome.smarthome_client.models.ItemlistItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistActivity extends Activity
{
    // Constants
    private final String LOGTAG = getClass().getSimpleName();
    public static final int EDITLISTACTIVITY_REQUESTCODE = 1;

    // Database
    private DbHelper mDbHelper;

    // Views
    private AutoCompleteTextView mItemlistSuggestionField_acTextView;
    private ListView mItemlist_listView;
    private ActionBar mActionBar;

    // Navigation drawer
    private NavigationDrawerItem_ArrayList mNavigationDrawerItems;
    private DrawerLayout mNavigationDrawerLayout;
    private RelativeLayout mNavigationDrawer;
    private ListView mNavigationDrawer_itemlists_listView;
    private Itemlist_NavigationDrawer_Adapter mNavigationDrawerAdapter;
    private ActionBarDrawerToggle mNavigationDrawerToggle;
    private Comparator<NavigationDrawerItem> mNavigationDrawerItemComparator;
    private NavigationDrawerItem newlyCreatedItem = null;
    private String preEditedItemName = null;
    private String deleteThisList = null;

    // Searchbar
    private ArrayList<String> mSuggestions;
    private Suggestion_Adapter mSuggestionAdapter;
    private Comparator<String> mSuggestionComparator;

    // Shoppinglist
    private ItemlistItem_ArrayList mSelectedItemlist;
    private String mSelectedItemlistTitle;
    private Itemlist_Adapter mItemlistAdapter;
    private Comparator<ItemlistItem> mItemlistItemComparator;

    // Shared preferences
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;


    /**
     * ON CREATE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(LOGTAG, "*** Start of onCreate() ***");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        // Instantiate the database helper
        mDbHelper = new DbHelper(getApplicationContext());

        // SharedPreferences
        prefs = getPreferences(MODE_PRIVATE);


        // Get the selected itemlist and set the title
        mSelectedItemlistTitle = prefs.getString(getString(R.string.selectedItemList), "");
        mActionBar = getActionBar();
        mActionBar.setTitle(mSelectedItemlistTitle);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);


        // Navigation drawer
        mNavigationDrawer_itemlists_listView = (ListView) findViewById(R.id.drawer_lists);
        mNavigationDrawerItems = new NavigationDrawerItem_ArrayList();
        mNavigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer = (RelativeLayout) findViewById(R.id.left_drawer);
        mNavigationDrawerItemComparator = NavigationDrawerItem_Comparator.getNameComparator(true);
        mNavigationDrawerAdapter = new Itemlist_NavigationDrawer_Adapter(getApplicationContext(), mNavigationDrawerItems, mNavigationDrawerItemComparator, this);
        mNavigationDrawer_itemlists_listView.setAdapter(mNavigationDrawerAdapter);
        mNavigationDrawer_itemlists_listView.setOnItemClickListener(new Itemlist_NavigationDrawerItem_ClickListener(this));
        mNavigationDrawerToggle = new Itemlist_NavigationDrawer_Toggle(this, mNavigationDrawerLayout, R.string.drawer_open, R.string.drawer_open);
        mNavigationDrawerLayout.addDrawerListener(mNavigationDrawerToggle);


        // Get the searchbar and populate the hint with the array of all shoppinglist items
        mItemlistSuggestionField_acTextView = (AutoCompleteTextView) findViewById(R.id.item_searchbar_textView);
        mSuggestionComparator = SuggestionItem_Comparator.getNameComparator(true);
        mSuggestions = new ArrayList<>();
        mSuggestionAdapter = new Suggestion_Adapter(this, mSuggestions, mSuggestionComparator, mItemlistSuggestionField_acTextView, this);
        mItemlistSuggestionField_acTextView.setAdapter(mSuggestionAdapter);
        mItemlistSuggestionField_acTextView.setThreshold(1);
        mItemlistSuggestionField_acTextView.addTextChangedListener(new SuggestionField_TextWatcher(this, mItemlistSuggestionField_acTextView));


        // Get the listview for the current shoppinglist and populate it with the shoppinglist array
        mItemlist_listView = (ListView) findViewById(R.id.shoppinglist_listview);
        mItemlistItemComparator = ItemlistItem_Comparator.getDateComparator(false);
        mSelectedItemlist = new ItemlistItem_ArrayList();
        mItemlistAdapter = new Itemlist_Adapter(this, mSelectedItemlist, mItemlistItemComparator);
        mItemlist_listView.setAdapter(mItemlistAdapter);

        initiateButtonOnClickListeners();
    }

    private void initiateButtonOnClickListeners()
    {
        findViewById(R.id.hide_keyboard_btn).setOnClickListener(new HideKeyboard_ButtonClickListener(getApplicationContext(), mItemlistSuggestionField_acTextView));
        findViewById(R.id.remove_marked_items_btn).setOnClickListener(new RemoveMarkedItems_ButtonClickListener(mSelectedItemlist, mItemlistAdapter));
        findViewById(R.id.remove_all_items_btn).setOnClickListener(new RemoveAllItems_ButtonClickListener(mItemlistAdapter));
        findViewById(R.id.add_item_btn).setOnClickListener(new Add_ButtonClickListener(this));
        findViewById(R.id.navigation_drawer_main_menu).setOnClickListener(new MainMenu_ButtonClickListener(this));
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
     * ON RESUME
     */
    @Override
    protected void onResume()
    {
        Log.i(LOGTAG, "*** Start of onResume() ***");
        super.onResume();

        mSuggestionAdapter.clear();
        mItemlistAdapter.clear();

        updateListsAndItems();

        // Check if a list is created or edited
        if (newlyCreatedItem != null)
        {
            if (preEditedItemName != null)
            {
                mNavigationDrawerAdapter.editItem(preEditedItemName, newlyCreatedItem);
                mSelectedItemlistTitle = newlyCreatedItem.getItemName();
                mActionBar.setTitle(mSelectedItemlistTitle);
                preEditedItemName = null;
            }
            else
            {
                mNavigationDrawerAdapter.add(newlyCreatedItem);
            }
            newlyCreatedItem = null;
        }

        // Check if a list should be deleted
        if (deleteThisList != null)
        {
            mSuggestionAdapter.clear();
            mItemlistAdapter.clear();
            mNavigationDrawerAdapter.remove(deleteThisList);
            if (deleteThisList.equals(mSelectedItemlistTitle))
            {
                mSelectedItemlistTitle = "";
                mActionBar.setTitle(mSelectedItemlistTitle);
            }
            deleteThisList = null;
        }
    }


    /**
     * ON PAUSE
     */
    @Override
    protected void onPause()
    {
        Log.i(LOGTAG, "*** Start of onPause() ***");
        super.onPause();

        saveCurrentListAndItems();
    }


    /**
     * ON STOP
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(LOGTAG, "*** Start of onStop() ***");

        prefEditor = prefs.edit();
        prefEditor.putString(getString(R.string.selectedItemList), mSelectedItemlistTitle);
        prefEditor.apply();
    }


    /**
     * ON DESTROY
     */
    @Override
    protected void onDestroy()
    {
        Log.i(LOGTAG, "*** Start of onDestroy() ***");
        super.onDestroy();

        mDbHelper.close();
    }


    /**
     * Called when the text in the autocomplete TextView should be added as a
     * new item to the current list
     */
    public void onAddItem(View view)
    {
        Log.i(LOGTAG, "In method onAddItem()");
        String itemName = mItemlistSuggestionField_acTextView.getText().toString().trim().replace(System.getProperty("line.separator"), "");

        if (itemName.equals(""))
        {
            return;
        }

        if (mSelectedItemlistTitle.equals(""))
        {
            Toast.makeText(view.getContext(), "Please select or create a list from the sidebar", Toast.LENGTH_SHORT).show();
            mItemlistSuggestionField_acTextView.setText("");
            return;
        }

        if (!mSuggestionAdapter.contains(itemName))
        {
            // Add the item as a suggested item
            mSuggestionAdapter.add(itemName);
        }

        if (mItemlistAdapter.contains(itemName))
        {
            // The item already exists in the list
            Toast.makeText(view.getContext(), itemName + " is already added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // The item does not exist in the list, it should be added
            mItemlistAdapter.add(new ItemlistItem(itemName));
        }

        // Clear the searchbar
        mItemlistSuggestionField_acTextView.setText("");
    }


    /**
     * Empty method to prevent the possibility of clicking 'through' the navigation drawer.
     * */
    public void onNavDrawerClick(View view)
    {
        Log.i(LOGTAG, "In method onNavDrawerClick()");
    }


    /**
     * An item in the navigation drawer is clicked
     */
    public void onDrawerItemClick(int position)
    {
        Log.i(LOGTAG, "In method onDrawerItemClick()");
        saveCurrentListAndItems();
        mSelectedItemlistTitle = mNavigationDrawerAdapter.getItem(position).getItemName();
        updateListsAndItems();

        // Highlight the selected item, update the title, and close the drawer
        mNavigationDrawer_itemlists_listView.setItemChecked(position, true);
        mActionBar.setTitle(mSelectedItemlistTitle);
        mNavigationDrawerLayout.closeDrawer(mNavigationDrawer);
    }


    /**
     * Updates the database with the values from the currently selected
     * itemlist 'mSelectedItemlistTitle'. Deletes the list from the db first to ensure correct
     * values are there after the current ones are inserted.
     * */
    public void saveCurrentListAndItems()
    {
        Log.i(LOGTAG, "In method saveCurrentListAndItems()");
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();


        DbHelper.deleteSuggestionsForItemlist(mDb, mSelectedItemlistTitle);
        DbHelper.deleteItemsInItemlist(mDb, mSelectedItemlistTitle);
        DbHelper.deleteItemLists(mDb);


        Log.i("TEST", "mNavigationDrawerItems.size() = " + mNavigationDrawerItems.size());
        DbHelper.insertAllItemLists(mDb, mNavigationDrawerItems);

        Log.i("TEST", "mSelectedItemlist.size() = " + mSelectedItemlist.size());
        DbHelper.insertAllItems(mDb, mSelectedItemlist, mSelectedItemlistTitle);

        Log.i("TEST", "mSuggestions.size() = " + mSuggestionAdapter.getList().size());
        DbHelper.insertAllSuggestions(mDb, mSuggestionAdapter.getList(), mSelectedItemlistTitle);


        mDb.close();
    }


    /**
     * Updates the UI with the content from the database corresponding to the current
     * selected itemlist 'mSelectedItemlistTitle'.
     * */
    public void updateListsAndItems()
    {
        Log.i(LOGTAG, "In method updateListsAndItems()");
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();

        NavigationDrawerItem_ArrayList itemlistsFromDb = DbHelper.getItemLists(mDb);
        ItemlistItem_ArrayList itemsFromDb = DbHelper.getItemsForList(mDb, mSelectedItemlistTitle);
        ArrayList<String> itemSuggestionsFromDb = DbHelper.getSuggestionsForList(mDb, mSelectedItemlistTitle);

        mDb.close();

        mNavigationDrawerAdapter.clear();
        if (itemlistsFromDb.size() > 0)
        {
            for (NavigationDrawerItem item : itemlistsFromDb)
            {
                mNavigationDrawerAdapter.add(item);
            }
        }

        mItemlistAdapter.clear();
        if (itemsFromDb.size() > 0)
        {
            for (ItemlistItem item : itemsFromDb)
            {
                mItemlistAdapter.add(item);
            }
        }

        mSuggestionAdapter.clear();
        if (itemSuggestionsFromDb.size() > 0)
        {
            for (String itemName : itemSuggestionsFromDb)
            {
                mSuggestionAdapter.add(itemName);
            }
        }
    }

    /**
     * When 'Create new list' is clicked in the navigation drawer
     * */
    public void onCreateNewItemlist(View view)
    {
        Log.i(LOGTAG, "In method onCreateNewItemlist()");
        Intent editList_intent = new Intent(view.getContext(), EditListActivity.class);
        startActivityForResult(editList_intent, EDITLISTACTIVITY_REQUESTCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(LOGTAG, "In method onActivityResult()");
        if (requestCode == EDITLISTACTIVITY_REQUESTCODE)
        {
            if (resultCode == RESULT_OK)
            {
                String delete = data.getStringExtra("deleted");
                if (delete != null)
                {
                    deleteThisList = delete;
                }
                else
                {
                    String newListTitle = data.getStringExtra("newListTitle");
                    int icon = data.getIntExtra("icon", R.drawable.ic_crop_7_5_black_24dp);
                    boolean publicList = data.getBooleanExtra("listPublic", false);
                    boolean edited = data.getBooleanExtra("edit", false);

                    if (mNavigationDrawerAdapter.contains(newListTitle))
                    {
                        Toast.makeText(getApplicationContext(), newListTitle + " already exists as a list", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (edited)
                    {
                        preEditedItemName = mSelectedItemlistTitle;
                    }
                    newlyCreatedItem = new NavigationDrawerItem(newListTitle, icon, publicList);
                }
            }
        }
    }


    public String getActionBarTitle()
    {
        return mSelectedItemlistTitle;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mNavigationDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mNavigationDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mNavigationDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
