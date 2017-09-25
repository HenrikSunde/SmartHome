package smarthome.smarthome_client.models;

import java.util.Date;

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

    public ItemlistItem(String name)
    {
        this(-1, name, -1);
    }

    public ItemlistItem(int id, String name, int list_id)
    {
        this(id, name, false, list_id);
    }

    public ItemlistItem(int id, String name, boolean marked, int list_id)
    {
        this(id, name, marked, new Date(), list_id);
    }

    public ItemlistItem(int id, String name, boolean marked, Date addDate, int list_id)
    {
        this.name = name;
        this.marked = marked;
        dateAddedFormatted = dateToDateFormat(addDate);
    }

    public ItemlistItem(int id, String name, boolean marked, String dateAddedFormatted, int list_id)
    {
        this.name = name;
        this.marked = marked;
        this.dateAddedFormatted = dateAddedFormatted;
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
        return name + ", marked=" + marked + " - added=" + dateAddedFormatted;
    }
}
