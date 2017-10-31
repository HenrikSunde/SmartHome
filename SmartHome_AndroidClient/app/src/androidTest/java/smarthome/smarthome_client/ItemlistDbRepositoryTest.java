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
import smarthome.smarthome_client.database.ItemlistDbRepository;
import smarthome.smarthome_client.database.interfaces.IItemlistRepository;
import smarthome.smarthome_client.models.ItemlistTitleItem;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ItemlistDbRepositoryTest
{
    private Context appContext;
    private DbHelper testDbHelper;
    private IItemlistRepository itemlistRepo;

    public ItemlistDbRepositoryTest()
    {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Before
    public void setUp()
    {
        testDbHelper = new DbHelper(appContext);
        itemlistRepo = new ItemlistDbRepository(testDbHelper);
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
    public void test_add_itemlistTitleItem()
    {
        ItemlistTitleItem item = new ItemlistTitleItem("TestItem", 0, false, false, 0);

        itemlistRepo.add(item);

        ItemlistTitleItem dbItem = itemlistRepo.get("TestItem");

        assertNotNull(dbItem);
        assertEquals(item.getName(), dbItem.getName());
        assertEquals(item.getIconResourceID(), dbItem.getIconResourceID());
        assertEquals(item.isPublicList(), dbItem.isPublicList());
        assertEquals(item.isSuggestions(), dbItem.isSuggestions());
        assertEquals(item.getCount(), dbItem.getCount());
        assertEquals(-1, item.getId());
        assertTrue(dbItem.getId() > 0);
    }

    @Test
    public void test_get_itemlistTitleItems_on_name()
    {
        ItemlistTitleItem item = new ItemlistTitleItem("TestItem", 0, false, false, 0);

        itemlistRepo.add(item);

        ItemlistTitleItem dbItem = itemlistRepo.get("TestItem");

        assertNotNull(dbItem);
        assertEquals(item.getName(), dbItem.getName());
        assertEquals(item.getIconResourceID(), dbItem.getIconResourceID());
        assertEquals(item.isPublicList(), dbItem.isPublicList());
        assertEquals(item.isSuggestions(), dbItem.isSuggestions());
        assertEquals(item.getCount(), dbItem.getCount());
        assertEquals(-1, item.getId());
        assertTrue(dbItem.getId() > 0);
    }

    @Test
    public void test_get_itemlistTitleItems_on_id()
    {
        ItemlistTitleItem item = new ItemlistTitleItem("TestItem", 0, false, false, 0);

        itemlistRepo.add(item);

        ItemlistTitleItem dbItem = itemlistRepo.get(1);

        assertNotNull(dbItem);
        assertEquals(item.getName(), dbItem.getName());
        assertEquals(item.getIconResourceID(), dbItem.getIconResourceID());
        assertEquals(item.isPublicList(), dbItem.isPublicList());
        assertEquals(item.isSuggestions(), dbItem.isSuggestions());
        assertEquals(item.getCount(), dbItem.getCount());
        assertEquals(-1, item.getId());
        assertEquals(1, dbItem.getId());
    }

    @Test
    public void test_get_all_itemlistTitleItems()
    {
        ItemArraylist<ItemlistTitleItem> items = new ItemArraylist<>();
        items.add(new ItemlistTitleItem("TestItem1", 0, false, false, 0));
        items.add(new ItemlistTitleItem("TestItem2", 0, false, false, 0));
        items.add(new ItemlistTitleItem("TestItem3", 0, false, false, 0));

        itemlistRepo.add(items);

        ItemArraylist<ItemlistTitleItem> dbItems = itemlistRepo.get();

        assertNotNull(dbItems);
        assertEquals(items.size(), dbItems.size());
    }

    @Test
    public void test_update_itemlistTitleItem()
    {
        ItemlistTitleItem item = new ItemlistTitleItem("TestItem", 0, false, false, 0);

        itemlistRepo.add(item);

        item = itemlistRepo.get("TestItem");
        item.setName("Edited");
        item.setIconResourceID(1);
        item.setPublicList(true);
        item.setSuggestions(true);
        item.setCount(1);

        itemlistRepo.update(item);
        ItemlistTitleItem dbItem = itemlistRepo.get(item.getId());

        assertNotNull(dbItem);
        assertEquals(item.getName(), dbItem.getName());
        assertEquals(item.getIconResourceID(), dbItem.getIconResourceID());
        assertEquals(item.isPublicList(), dbItem.isPublicList());
        assertEquals(item.isSuggestions(), dbItem.isSuggestions());
        assertEquals(0, dbItem.getCount());
        assertEquals(item.getId(), dbItem.getId());
    }

    @Test
    public void test_delete_itemlistTitleItem() throws Exception
    {
        ItemlistTitleItem item = new ItemlistTitleItem("TestItem", 0, false, false, 0);

        itemlistRepo.add(item);

        item = itemlistRepo.get("TestItem");

        itemlistRepo.delete(item);

        assertNull(itemlistRepo.get("TestItem"));
    }
}
