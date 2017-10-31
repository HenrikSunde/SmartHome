package smarthome.smarthome_client;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import smarthome.smarthome_client.arraylists.ItemArraylist;
import smarthome.smarthome_client.database.DbHelper;
import smarthome.smarthome_client.database.ItemDbRepository;
import smarthome.smarthome_client.models.ItemlistItem;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ItemDbRepositoryTest
{
    private Context appContext;
    private DbHelper testDbHelper;
    private ItemDbRepository itemRepo;

    public ItemDbRepositoryTest()
    {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Before
    public void setUp()
    {
        testDbHelper = new DbHelper(appContext);
        itemRepo = new ItemDbRepository(testDbHelper);
    }

    @After
    public void tearDown()
    {
        testDbHelper.onUpgrade(testDbHelper.getWritableDatabase(), 0, 0);
        testDbHelper.close();
    }



    /**
     * TESTS BELOW
     * */


    @Test
    public void alwaysPass() throws Exception
    {
        assertTrue(true);
    }


    @Test
    public void test_add_itemlistItem() throws Exception
    {
        ItemlistItem item = new ItemlistItem("Test", 1);

        itemRepo.add(item);

        ItemlistItem dbItem = itemRepo.get(1, "Test");

        assertNotNull(dbItem);
        assertEquals(item.getName(), dbItem.getName());
        assertEquals(item.getList_id(), dbItem.getList_id());
        assertEquals(1, dbItem.getId());
        assertEquals(false, dbItem.isMarked());
        assertNotNull(dbItem.getDateAddedFormatted());
    }

    @Test
    public void test_get_all_on_listId() throws Exception
    {
        ItemArraylist<ItemlistItem> items = new ItemArraylist<>();
        items.add(new ItemlistItem("Test1", 1));
        items.add(new ItemlistItem("Test2", 1));
        items.add(new ItemlistItem("Test3", 1));
        items.add(new ItemlistItem("Test4", 2));
        items.add(new ItemlistItem("Test5", 3));

        itemRepo.add(items);

        ItemArraylist<ItemlistItem> dbItems = itemRepo.get(1);

        assertNotNull(dbItems);
        assertEquals(3, dbItems.size());
    }

    @Test
    public void test_delete_all_on_listId() throws Exception
    {
        ItemArraylist<ItemlistItem> items = new ItemArraylist<>();
        items.add(new ItemlistItem("Test1", 1));
        items.add(new ItemlistItem("Test2", 1));
        items.add(new ItemlistItem("Test3", 1));
        items.add(new ItemlistItem("Test4", 2));
        items.add(new ItemlistItem("Test5", 3));

        itemRepo.add(items);

        itemRepo.delete(1);

        assertEquals(0, itemRepo.get(1).size());
    }

    @Test
    public void test_get_count() throws Exception
    {
        ItemArraylist<ItemlistItem> items = new ItemArraylist<>();
        items.add(new ItemlistItem("Test1", 1));
        items.add(new ItemlistItem("Test2", 1));
        items.add(new ItemlistItem("Test3", 1));
        items.add(new ItemlistItem("Test4", 2));
        items.add(new ItemlistItem("Test5", 3));

        itemRepo.add(items);

        ItemArraylist<ItemlistItem> dbItems = itemRepo.get(1);

        assertEquals(dbItems.size(), itemRepo.count(1));
    }
}
