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
import smarthome.smarthome_client.database.SuggestionDbRepository;
import smarthome.smarthome_client.models.Suggestion;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SuggestionDbRepositoryTest
{
    private Context appContext;
    private DbHelper testDbHelper;
    private SuggestionDbRepository suggestionRepo;

    public SuggestionDbRepositoryTest()
    {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Before
    public void setUp()
    {
        testDbHelper = new DbHelper(appContext);
        suggestionRepo = new SuggestionDbRepository(testDbHelper);
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
    public void test_add_suggestion() throws Exception
    {
        Suggestion suggestion = new Suggestion("Test", 2);

        suggestionRepo.add(suggestion);

        Suggestion dbSuggestion = suggestionRepo.get(2, "Test");

        assertNotNull(dbSuggestion);
        assertEquals(suggestion.getName(), dbSuggestion.getName());
        assertEquals(suggestion.getList_id(), dbSuggestion.getList_id());
        assertEquals(1, dbSuggestion.getId());
    }


    @Test
    public void test_get_suggestions() throws Exception
    {
        ItemArraylist<Suggestion> suggestions = new ItemArraylist<>();
        suggestions.add(new Suggestion("Test1", 2));
        suggestions.add(new Suggestion("Test2", 2));
        suggestions.add(new Suggestion("Test3", 2));
        suggestions.add(new Suggestion("Test4", 3));
        suggestions.add(new Suggestion("Test5", 4));

        suggestionRepo.add(suggestions);

        ItemArraylist<Suggestion> dbSuggestions = suggestionRepo.get(2);

        assertNotNull(dbSuggestions);
        assertEquals(3, dbSuggestions.size());
        assertTrue(dbSuggestions.contains("Test1"));
        assertTrue(dbSuggestions.contains("Test2"));
        assertTrue(dbSuggestions.contains("Test3"));
    }


    @Test
    public void test_get_suggestion() throws Exception
    {
        ItemArraylist<Suggestion> suggestions = new ItemArraylist<>();
        suggestions.add(new Suggestion("Test1", 2));
        suggestions.add(new Suggestion("Test2", 2));
        suggestions.add(new Suggestion("Test3", 2));
        suggestions.add(new Suggestion("Test1", 3));
        suggestions.add(new Suggestion("Test1", 4));

        suggestionRepo.add(suggestions);

        Suggestion dbSuggestion = suggestionRepo.get(2, "Test1");

        assertNotNull(dbSuggestion);
        assertEquals(1, dbSuggestion.getId());
    }


    @Test
    public void test_delete_suggestions() throws Exception
    {
        ItemArraylist<Suggestion> suggestions = new ItemArraylist<>();
        suggestions.add(new Suggestion("Test1", 2));
        suggestions.add(new Suggestion("Test2", 2));
        suggestions.add(new Suggestion("Test3", 2));
        suggestions.add(new Suggestion("Test4", 3));
        suggestions.add(new Suggestion("Test5", 4));

        suggestionRepo.add(suggestions);

        suggestionRepo.delete(2);

        assertEquals(0, suggestionRepo.get(2).size());
    }
}
