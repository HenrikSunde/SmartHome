package smarthome.smarthome_client.activity.application.itemlist;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.actionbardrawertoggles.Itemlist_NavigationDrawer_Toggle;
import smarthome.smarthome_client.adapters.SmartHomeBaseAdapter;
import smarthome.smarthome_client.adapters.Suggestion_Adapter;
import smarthome.smarthome_client.adapters.Itemlist_Adapter;
import smarthome.smarthome_client.adapters.Itemlist_NavigationDrawer_Adapter;
import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.comparators.ComparatorFactory;
import smarthome.smarthome_client.database.DbHelper;
import smarthome.smarthome_client.database.ItemDbRepository;
import smarthome.smarthome_client.database.ItemlistDbRepository;
import smarthome.smarthome_client.database.SuggestionDbRepository;
import smarthome.smarthome_client.database.interfaces.IItemRepository;
import smarthome.smarthome_client.database.interfaces.IItemlistRepository;
import smarthome.smarthome_client.database.interfaces.ISuggestionRepository;
import smarthome.smarthome_client.listeners.SuggestionField_TextWatcher;
import smarthome.smarthome_client.models.ItemlistTitleItem;
import smarthome.smarthome_client.models.ItemlistItem;
import smarthome.smarthome_client.models.Suggestion;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistActivity extends Activity
{
    // Constants
    private final String LOGTAG = getClass().getSimpleName();
    public static final int EDITLISTACTIVITY_REQUESTCODE = 1;
    public static final int EDITLIST_DELETED = 1;
    public static final int EDITLIST_EDITED = 2;
    public static final int EDITLIST_NEWLIST = 3;

    // Database
    private DbHelper mDbHelper;
    private static IItemlistRepository mItemlistRepo;
    private static IItemRepository mItemRepo;
    private static ISuggestionRepository mSuggestionRepo;

    // Views
    private AutoCompleteTextView mItemlistSuggestionField_acTextView;
    private ListView mItemlist_listView;

    // Actionbar
    private ActionBar mActionBar;
    private ImageView mHome_Button;
    private ImageView mNavigationDrawer_Button;
    private TextView mActionBarTitle_TextView;

    // Navigation drawer
    private ItemArraylist<ItemlistTitleItem> mAllItemlistTitleItems;
    private DrawerLayout mNavigationDrawerLayout;
    private RelativeLayout mNavigationDrawer;
    private ListView mNavigationDrawer_itemlists_listView;
    private ActionBarDrawerToggle mNavigationDrawerToggle;

    // Currently selected
    private ItemlistTitleItem mSelectedItemlistTitleItem;
    private ItemArraylist<ItemlistItem> mSelectedItemlistItems;
    private ItemArraylist<Suggestion> mSelectedSuggestions;


    private SmartHomeBaseAdapter<ItemlistTitleItem> mNavigationDrawerAdapter;
    private Suggestion_Adapter mSuggestionAdapter;
    private SmartHomeBaseAdapter<ItemlistItem> mItemlistAdapter;

    // Comparator
    private ComparatorFactory mComparatorFactory = new ComparatorFactory();

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
        mItemlistRepo = new ItemlistDbRepository(mDbHelper);
        mSuggestionRepo = new SuggestionDbRepository(mDbHelper);
        mItemRepo = new ItemDbRepository(mDbHelper);

        // SharedPreferences
        prefs = getPreferences(MODE_PRIVATE);

        // Get the selected itemlist
        String savedSelectedItemlistTitle = prefs.getString(getString(R.string.selectedItemList), "");
        if (savedSelectedItemlistTitle.equals(""))
        {
            mSelectedItemlistTitleItem = new ItemlistTitleItem("", 0, false, false);
        }
        else
        {
            mSelectedItemlistTitleItem = mItemlistRepo.get(savedSelectedItemlistTitle);
        }


        // Actionbar magic
        mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        View actionBarLayout = getLayoutInflater().inflate(R.layout.actionbar_menu, null);
        mActionBar.setCustomView(actionBarLayout);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        mHome_Button = (ImageView) findViewById(R.id.home_imageView);
        mNavigationDrawer_Button = (ImageView) findViewById(R.id.open_navigation_drawer_imageView);
        mActionBarTitle_TextView = (TextView) findViewById(R.id.action_bar_title_textView);


        // Navigation drawer
        mNavigationDrawer_itemlists_listView = (ListView) findViewById(R.id.drawer_lists);
        mAllItemlistTitleItems = new ItemArraylist<>();
        mNavigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer = (RelativeLayout) findViewById(R.id.right_drawer);
        mNavigationDrawerAdapter = new Itemlist_NavigationDrawer_Adapter(this, mAllItemlistTitleItems, mComparatorFactory.getTitleItemNameComparator(true));
        mNavigationDrawer_itemlists_listView.setAdapter(mNavigationDrawerAdapter);
        mNavigationDrawerToggle = new Itemlist_NavigationDrawer_Toggle(this, mNavigationDrawerLayout, R.string.drawer_open, R.string.drawer_open);
        mNavigationDrawerLayout.addDrawerListener(mNavigationDrawerToggle);


        // Get the searchbar and populate the hint with the array of all shoppinglist items
        mItemlistSuggestionField_acTextView = (AutoCompleteTextView) findViewById(R.id.item_searchbar_textView);
        mSelectedSuggestions = new ItemArraylist<>();
        mSuggestionAdapter = new Suggestion_Adapter(this, mSelectedSuggestions, mComparatorFactory.getSuggestionNameComparator(true));
        mItemlistSuggestionField_acTextView.setAdapter(mSuggestionAdapter);
        mItemlistSuggestionField_acTextView.setThreshold(1);
        mItemlistSuggestionField_acTextView.addTextChangedListener(new SuggestionField_TextWatcher(this, mItemlistSuggestionField_acTextView));


        // Get the listview for the current shoppinglist and populate it with the shoppinglist array
        mItemlist_listView = (ListView) findViewById(R.id.shoppinglist_listview);
        mSelectedItemlistItems = new ItemArraylist<>();
        mItemlistAdapter = new Itemlist_Adapter(this, mSelectedItemlistItems, mComparatorFactory.getDateComparator(false));
        mItemlist_listView.setAdapter(mItemlistAdapter);

        initiateButtonOnClickListeners();
    }

    private void initiateButtonOnClickListeners()
    {
        //
        findViewById(R.id.hide_keyboard_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        //
        findViewById(R.id.remove_marked_items_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
                            for (ItemlistItem item : mSelectedItemlistItems)
                            {
                                if (item.isMarked())
                                {
                                    removeTheseItems.add(item.getName());
                                }
                            }

                            // Remove the marked items
                            for (String itemName : removeTheseItems)
                            {
                                mItemlistAdapter.remove(itemName);
                            }

                            if (!removeTheseItems.isEmpty())
                            {
                                mItemlistAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                };
                if (!mSelectedItemlistItems.isEmpty())
                {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setMessage("Remove all marked items from the list?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        //
        findViewById(R.id.remove_all_items_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == DialogInterface.BUTTON_POSITIVE)
                        {
                            mItemlistAdapter.clear();
                            mItemlistAdapter.notifyDataSetChanged();
                        }
                    }
                };
                if (!mItemlistAdapter.isEmpty())
                {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setMessage("Clear the list?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        //
        findViewById(R.id.add_item_btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAddItem(v);
            }
        });

        //
        mHome_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        //
        mNavigationDrawer_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!mNavigationDrawerLayout.isDrawerOpen(Gravity.END))
                {
                    mNavigationDrawerLayout.openDrawer(Gravity.END);
                }
                else
                {
                    mNavigationDrawerLayout.closeDrawer(Gravity.END);
                }
            }
        });

        //
        mNavigationDrawer_itemlists_listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                onDrawerItemClick(position);
            }
        });
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

        updateSelectedLists();
    }


    /**
     * ON PAUSE
     */
    @Override
    protected void onPause()
    {
        Log.i(LOGTAG, "*** Start of onPause() ***");
        super.onPause();
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

        if (getSelectedItemlistTitle().equals(""))
        {
            Toast.makeText(view.getContext(), "Please select or create a list from the sidebar", Toast.LENGTH_SHORT).show();
            setAutoCompleteTextViewText("");
            return;
        }

        if (!mSuggestionAdapter.contains(itemName))
        {
            // Add the item as a suggested item
            Suggestion suggestion = new Suggestion(itemName, mSelectedItemlistTitleItem.getId());
            mSuggestionRepo.add(suggestion);
            mSuggestionAdapter.add(suggestion);
            mSuggestionAdapter.notifyDataSetChanged();
        }

        if (mItemlistAdapter.contains(itemName))
        {
            Toast.makeText(view.getContext(), itemName + " is already added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // The item does not exist in the list, it should be added
            ItemlistItem item = new ItemlistItem(itemName);
            mItemRepo.add(item);
            mItemlistAdapter.add(item);
            mNavigationDrawerAdapter.notifyDataSetChanged();
        }

        setAutoCompleteTextViewText("");
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

        if (mSelectedItemlistTitleItem != null && mItemlistRepo.get(mSelectedItemlistTitleItem.getId()) != null)
        {
            // If a list is selected, and the selected list is already in the db
            // Delete selected lists from db
            mSuggestionRepo.delete(mSelectedItemlistTitleItem.getId());
            mItemRepo.delete(mSelectedItemlistTitleItem.getId());

            // Save selected lists to db
            mSuggestionRepo.add(mSelectedSuggestions);
            mItemRepo.add(mSelectedItemlistItems);
        }

        if (position < 0)
        {
            // No list is selected
            setSelectedItemlistTitle(null);
        }
        else
        {
            // A list is selected
            setSelectedItemlistTitle(mNavigationDrawerAdapter.getItem(position));
            if (mItemlistRepo.get(mSelectedItemlistTitleItem.getName()) == null)
            {
                // If the list does not exist in the database, add it
                mItemlistRepo.add(mSelectedItemlistTitleItem);
            }
            else
            {
                // If the list exists in the database, update it
                mItemlistRepo.update(mSelectedItemlistTitleItem);
            }
        }
        mNavigationDrawer_itemlists_listView.setItemChecked(position, true);

        updateSelectedLists();
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
            if (resultCode == EDITLIST_DELETED)
            {
                String delete = data.getStringExtra("deleted");
                mItemlistRepo.delete(mItemlistRepo.get(delete));
                onDrawerItemClick(-1);
            }

            else if (resultCode == EDITLIST_EDITED)
            {
                // Get values from the intent
                String originalListName = data.getStringExtra("originalList");
                String newListTitle = data.getStringExtra("newListTitle");
                int icon = data.getIntExtra("icon", R.drawable.ic_crop_7_5_black_24dp);
                boolean publicList = data.getBooleanExtra("listPublic", false);
                boolean suggestion = data.getBooleanExtra("suggestions", false);

                if (mNavigationDrawerAdapter.contains(newListTitle))
                {
                    Toast.makeText(getApplicationContext(), newListTitle + " already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new item, add it or update the original item if it exists
                ItemlistTitleItem originalItem = mNavigationDrawerAdapter.getItem(originalListName);
                ItemlistTitleItem newItem = new ItemlistTitleItem(newListTitle, icon, publicList, suggestion);

                mNavigationDrawerAdapter.editItem(originalItem, newItem);

                onDrawerItemClick(mNavigationDrawerAdapter.getPosition(newItem));
            }

            else if (resultCode == EDITLIST_NEWLIST)
            {
                // Get values from the intent
                String newListTitle = data.getStringExtra("newListTitle");
                int icon = data.getIntExtra("icon", R.drawable.ic_crop_7_5_black_24dp);
                boolean publicList = data.getBooleanExtra("listPublic", false);
                boolean suggestion = data.getBooleanExtra("suggestions", false);

                if (mNavigationDrawerAdapter.contains(newListTitle))
                {
                    Toast.makeText(getApplicationContext(), newListTitle + " already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new item, add it
                ItemlistTitleItem newItem = new ItemlistTitleItem(newListTitle, icon, publicList, suggestion);
                mNavigationDrawerAdapter.add(newItem);
                mNavigationDrawerAdapter.notifyDataSetChanged();

                onDrawerItemClick(mNavigationDrawerAdapter.getPosition(newItem));
            }
        }
    }


    public void updateSelectedLists()
    {
        int selectedListId = -1;
        if (mSelectedItemlistTitleItem != null)
        {
            selectedListId = mSelectedItemlistTitleItem.getId();
        }

        // Clear the adapters
        mItemlistAdapter.clear();
        mSuggestionAdapter.clear();
        mItemlistAdapter.clear();

        ItemArraylist<ItemlistTitleItem> titleItems = mItemlistRepo.get();
        if (titleItems != null)
        {
            for (ItemlistTitleItem titleItem : titleItems)
            {
                titleItem.setCount(mItemlistRepo.count(titleItem.getId()));
                mNavigationDrawerAdapter.add(titleItem);
            }
        }

        if (selectedListId > 0)
        {
            for (Suggestion suggestion : mSuggestionRepo.get(selectedListId))
            {
                mSuggestionAdapter.add(suggestion);
            }

            for (ItemlistItem item : mItemRepo.get(selectedListId))
            {
                mItemlistAdapter.add(item);
            }
        }

        // Notify the adapters to update the UI
        mNavigationDrawerAdapter.notifyDataSetChanged();
        mSuggestionAdapter.notifyDataSetChanged();
        mItemlistAdapter.notifyDataSetChanged();
    }


    public String getSelectedItemlistTitle()
    {
        return mSelectedItemlistTitleItem.getName();
    }

    public void setSelectedItemlistTitle(ItemlistTitleItem titleItem)
    {
        mSelectedItemlistTitleItem = titleItem;
        mActionBarTitle_TextView.setText(getSelectedItemlistTitle());

        prefEditor = prefs.edit();
        prefEditor.putString(getString(R.string.selectedItemList), getSelectedItemlistTitle());
        prefEditor.apply();
    }


    public void setAutoCompleteTextViewText(String text)
    {
        mItemlistSuggestionField_acTextView.setText(text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
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
        if (item != null && item.getItemId() == android.R.id.home)
        {
            if (mNavigationDrawerLayout.isDrawerOpen(Gravity.END))
            {
                mNavigationDrawerLayout.closeDrawer(Gravity.END);
            }
            else
            {
                mNavigationDrawerLayout.openDrawer(Gravity.END);
            }
        }
        return true;
    }
}
