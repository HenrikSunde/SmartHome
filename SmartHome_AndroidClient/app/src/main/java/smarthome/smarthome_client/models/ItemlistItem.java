package smarthome.smarthome_client.models;

import java.util.Date;
import java.util.Locale;

import smarthome.smarthome_client.models.interfaces.NameableItem;

import static smarthome.smarthome_client.logic.Logic.dateToDateFormat;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistItem implements NameableItem
{
    private int id;
    private String name;
    private boolean marked;
    private String dateAddedFormatted;
    private int list_id;

    // Constructor for items that are created right now
    public ItemlistItem(String name, int list_id)
    {
        this.name = name;
        this.marked = false;
        this.dateAddedFormatted = dateToDateFormat(new Date());
        this.list_id = list_id;
    }

    // Constructor for items that already exist somewhere (e.g. database)
    public ItemlistItem(int id, String name, boolean marked, String dateAddedFormatted, int list_id)
    {
        this.id = id;
        this.name = name;
        this.marked = marked;
        this.dateAddedFormatted = dateAddedFormatted;
        this.list_id = list_id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isMarked()
    {
        return marked;
    }

    public void setMarked(boolean marked)
    {
        this.marked = marked;
    }

    public String getDateAddedFormatted()
    {
        return dateAddedFormatted;
    }

    public void setDateAddedFormatted(String dateAddedFormatted)
    {
        this.dateAddedFormatted = dateAddedFormatted;
    }

    public int getList_id()
    {
        return list_id;
    }

    public void setList_id(int list_id)
    {
        this.list_id = list_id;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.getDefault(), "id=%d - name=%s - marked=%s - dateAdded=%s - list_id=%d", id, name, marked, dateAddedFormatted, list_id);
    }
}
