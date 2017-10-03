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
    private DrawerLayout mNavigationDrawerLayout;
    private RelativeLayout mNavigationDrawer;
    private ListView mNavigationDrawer_itemlists_listView;
    private ActionBarDrawerToggle mNavigationDrawerToggle;

    // Currently selected
    private ItemlistTitleItem mSelectedItemlistTitleItem;

    // Adapters
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
        ItemArraylist<ItemlistTitleItem> mAllItemlistTitleItems = new ItemArraylist<>();
        mNavigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawer = (RelativeLayout) findViewById(R.id.right_drawer);
        mNavigationDrawerAdapter = new Itemlist_NavigationDrawer_Adapter(this, mAllItemlistTitleItems, mComparatorFactory.getTitleItemNameComparator(true));
        mNavigationDrawer_itemlists_listView.setAdapter(mNavigationDrawerAdapter);
        mNavigationDrawerToggle = new Itemlist_NavigationDrawer_Toggle(this, mNavigationDrawerLayout, R.string.drawer_open, R.string.drawer_open);
        mNavigationDrawerLayout.addDrawerListener(mNavigationDrawerToggle);


        // Get the searchbar and populate the hint with the array of all shoppinglist items
        mItemlistSuggestionField_acTextView = (AutoCompleteTextView) findViewById(R.id.item_searchbar_textView);
        ItemArraylist<Suggestion> mSelectedSuggestions = new ItemArraylist<>();
        mSuggestionAdapter = new Suggestion_Adapter(this, mSelectedSuggestions, mComparatorFactory.getSuggestionNameComparator(true));
        mItemlistSuggestionField_acTextView.setAdapter(mSuggestionAdapter);
        mItemlistSuggestionField_acTextView.setThreshold(1);
        mItemlistSuggestionField_acTextView.addTextChangedListener(new SuggestionField_TextWatcher(this, mItemlistSuggestionField_acTextView));


        // Get the listview for the current shoppinglist and populate it with the shoppinglist array
        mItemlist_listView = (ListView) findViewById(R.id.shoppinglist_listview);
        final ItemArraylist<ItemlistItem> mSelectedItemlistItems = new ItemArraylist<>();
        mItemlistAdapter = new Itemlist_Adapter(this, mSelectedItemlistItems, mComparatorFactory.getDateComparator(false));
        mItemlist_listView.setAdapter(mItemlistAdapter);


        // Get the selected itemlist
//        ItemArraylist<ItemlistTitleItem> allItemListsFromDb = mItemlistRepo.get();
//        mNavigationDrawerAdapter.setList(allItemListsFromDb);
//        mNavigationDrawerAdapter.notifyDataSetChanged();
//        setSelectedItemlist(mItemlistRepo.get(getSelectedItemlistTitle()));
//        updateUIForSelectedList();


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
                            ItemArraylist<ItemlistItem> removeTheseItems = new ItemArraylist<>();
                            for (ItemlistItem item : mItemlistAdapter.getList())
                            {
                                if (item.isMarked())
                                {
                                    removeTheseItems.add(item);
                                }
                            }

                            // Remove the marked items
                            for (ItemlistItem itemName : removeTheseItems)
                            {
                                mItemlistAdapter.remove(itemName);
                            }
                            mItemlistAdapter.notifyDataSetChanged();
                            mSelectedItemlistTitleItem.setCount(mItemlistAdapter.getCount());
                            mNavigationDrawerAdapter.notifyDataSetChanged();
                        }
                    }
                };
                if (!mItemlistAdapter.getList().isEmpty())
                {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Remove all marked items from the list?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
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
                            mSelectedItemlistTitleItem.setCount(mItemlistAdapter.getCount());
                            mNavigationDrawerAdapter.notifyDataSetChanged();
                        }
                    }
                };
                if (!mItemlistAdapter.isEmpty())
                {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Clear the list?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
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

        // Save selected lists in the database
        saveSelectedLists();

        prefEditor = prefs.edit();
        prefEditor.putString(getString(R.string.selectedItemList), getSelectedItemlistTitle());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(LOGTAG, "In method onActivityResult()");
        if (requestCode == EDITLISTACTIVITY_REQUESTCODE)
        {
            if (resultCode == EDITLIST_DELETED)
            {
                String deleteItemListTitle = data.getStringExtra("deleted");

                ItemlistTitleItem deletedItem = mNavigationDrawerAdapter.getItem(deleteItemListTitle);
                mItemlistRepo.delete(deletedItem);
                mNavigationDrawerAdapter.remove(deletedItem);
                mNavigationDrawerAdapter.notifyDataSetChanged();

                updateUIForSelectedList();

                Log.i(LOGTAG, "Itemlist deleted: " + deletedItem.toString());
            }

            else if (resultCode == EDITLIST_EDITED)
            {
                // Get values from the intent
                String originalListName = data.getStringExtra("originalList");
                String newListTitle = data.getStringExtra("newListTitle");
                int icon = data.getIntExtra("icon", R.drawable.ic_crop_7_5_black_24dp);
                boolean publicList = data.getBooleanExtra("listPublic", false);
                boolean suggestions = data.getBooleanExtra("suggestions", false);

                if (mNavigationDrawerAdapter.contains(newListTitle))
                {
                    onEditItemList(newListTitle, icon, publicList, suggestions);
                    Toast.makeText(getApplicationContext(), newListTitle + " already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new item, add it or update the original item if it exists
                ItemlistTitleItem originalItem = mNavigationDrawerAdapter.getItem(originalListName);
                ItemlistTitleItem newItem = new ItemlistTitleItem(newListTitle, icon, publicList, suggestions);

                mNavigationDrawerAdapter.editItem(originalItem, newItem);
                mNavigationDrawerAdapter.notifyDataSetChanged();
                mItemlistRepo.update(originalItem);

                setSelectedItemlist(originalItem);
                updateUIForSelectedList();

                Log.i(LOGTAG, "Itemlist edited: " + originalItem.toString());
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
                    onCreateNewItemlist(getCurrentFocus());
                    Toast.makeText(getApplicationContext(), newListTitle + " already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new item, add it
                ItemlistTitleItem newItem = new ItemlistTitleItem(newListTitle, icon, publicList, suggestion);

                mItemlistRepo.add(newItem);
                newItem = mItemlistRepo.get(newItem.getName());
                mNavigationDrawerAdapter.add(newItem);
                mNavigationDrawerAdapter.notifyDataSetChanged();

                setSelectedItemlist(newItem);
                updateUIForSelectedList();

                Log.i(LOGTAG, "New itemlist added: " + newItem.toString());
            }
        }
    }



    /**
     * An item in the navigation drawer is clicked
     */
    public void onDrawerItemClick(int position)
    {
        Log.i(LOGTAG, "In method onDrawerItemClick()");

        // Save selected lists in the database
        saveSelectedLists();

        ItemlistTitleItem selectedTitleItem = mNavigationDrawerAdapter.getItem(position);
        setSelectedItemlist(selectedTitleItem);
        updateUIForSelectedList();
        mNavigationDrawer_itemlists_listView.setItemChecked(position, true);
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
            mSuggestionAdapter.add(suggestion);
            mSuggestionAdapter.notifyDataSetChanged();

            Log.i(LOGTAG, "New suggestion added: " + suggestion.toString());
        }

        if (mItemlistAdapter.contains(itemName))
        {
            Toast.makeText(view.getContext(), itemName + " is already added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // The item does not exist in the list, it should be added
            ItemlistItem item = new ItemlistItem(itemName, mSelectedItemlistTitleItem.getId());
            mItemlistAdapter.add(item);
            mItemlistAdapter.notifyDataSetChanged();
            mSelectedItemlistTitleItem.setCount(mItemlistAdapter.getCount());
            mNavigationDrawerAdapter.notifyDataSetChanged();

            Log.i(LOGTAG, "New item added: " + item.toString());
        }

        setAutoCompleteTextViewText("");
    }


    public void saveSelectedLists()
    {
        if (mSelectedItemlistTitleItem != null)
        {
            mSuggestionRepo.delete(mSelectedItemlistTitleItem.getId());
            mSuggestionRepo.add(mSuggestionAdapter.getList());

            mItemRepo.delete(mSelectedItemlistTitleItem.getId());
            mItemRepo.add(mItemlistAdapter.getList());
        }
    }


    public void updateUIForSelectedList()
    {
        mSuggestionAdapter.clear();
        mItemlistAdapter.clear();

        if (mNavigationDrawerAdapter.contains(mSelectedItemlistTitleItem) && mSelectedItemlistTitleItem != null)
        {
            // A list that has not been deleted is selected, get items from db
            ItemArraylist<Suggestion> selectedSuggestions = mSuggestionRepo.get(mSelectedItemlistTitleItem.getId());
            ItemArraylist<ItemlistItem> selectedItems = mItemRepo.get(mSelectedItemlistTitleItem.getId());
            mSuggestionAdapter.setList(selectedSuggestions);
            mItemlistAdapter.setList(selectedItems);

            mSelectedItemlistTitleItem.setCount(mItemlistAdapter.getCount());
            mNavigationDrawerAdapter.notifyDataSetChanged();
        }

        mSuggestionAdapter.notifyDataSetChanged();
        mItemlistAdapter.notifyDataSetChanged();
    }



    /**
     * Empty method to prevent the possibility of clicking 'through' the navigation drawer.
     * */
    public void onNavDrawerClick(View view)
    {
        Log.i(LOGTAG, "In method onNavDrawerClick()");
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

    /**
     * When a list should be edited
     * */
    public void onEditItemList(String listTitle, int icon, boolean publicList, boolean suggestions)
    {
        Log.i(LOGTAG, "In method onEditItemList()");
        Intent editList_intent = new Intent(getApplicationContext(), EditListActivity.class);
        editList_intent.putExtra("edit", true);
        editList_intent.putExtra("itemlistName", listTitle);
        editList_intent.putExtra("icon", icon);
        editList_intent.putExtra("listPublic", publicList);
        editList_intent.putExtra("suggestions", suggestions);
        startActivityForResult(editList_intent, EDITLISTACTIVITY_REQUESTCODE);
    }


    public String getActionBarTitleText()
    {
        return mActionBarTitle_TextView.getText().toString();
    }

    public String getSelectedItemlistTitle()
    {
        if (mSelectedItemlistTitleItem != null)
        {
            return mSelectedItemlistTitleItem.getName();
        }
        return "";
    }

    public void setSelectedItemlist(ItemlistTitleItem titleItem)
    {
        mSelectedItemlistTitleItem = titleItem;
        if (titleItem == null)
        {
            // No list is selected
            mActionBarTitle_TextView.setText("");
        }
        else
        {
            // A list is selected
            mActionBarTitle_TextView.setText(getSelectedItemlistTitle());
        }
    }


    public void setAutoCompleteTextViewText(String text)
    {
        mItemlistSuggestionField_acTextView.setText(text);
    }

    public void removeSuggestion(Suggestion suggestion)
    {
        mSuggestionAdapter.remove(suggestion);
        mSuggestionAdapter.notifyDataSetChanged();
        mSuggestionRepo.delete(suggestion);
    }

    public void removeItemlistItem(ItemlistItem item)
    {
        mItemlistAdapter.remove(item);
        mItemlistAdapter.notifyDataSetChanged();
        mItemRepo.delete(item);
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
