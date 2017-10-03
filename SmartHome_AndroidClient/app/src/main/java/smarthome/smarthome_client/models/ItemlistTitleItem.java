package smarthome.smarthome_client.models;


import java.util.Locale;

import smarthome.smarthome_client.models.interfaces.NameableItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistTitleItem implements NameableItem
{
    private int id;
    private String name;
    private int iconResourceID;
    private boolean publicList;
    private boolean suggestions;
    private int count;

    public ItemlistTitleItem(String name, int iconResourceID, boolean publicList, boolean suggestions, int count)
    {
        this.id = -1;
        this.name = name;
        this.iconResourceID = iconResourceID;
        this.publicList = publicList;
        this.suggestions = suggestions;
        this.count = count;
    }

    public ItemlistTitleItem(String name, int iconResourceID, boolean publicList, boolean suggestions)
    {
        this(name, iconResourceID, publicList, suggestions, 0);
    }

    public ItemlistTitleItem(int id, String name, int iconResourceID, boolean publicList, boolean suggestions)
    {
        this(name, iconResourceID, publicList, suggestions, 0);
        this.id = id;
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

    public int getIconResourceID()
    {
        return iconResourceID;
    }

    public void setIconResourceID(int iconResourceID)
    {
        this.iconResourceID = iconResourceID;
    }

    public boolean isPublicList()
    {
        return publicList;
    }

    public void setPublicList(boolean publicList)
    {
        this.publicList = publicList;
    }

    public boolean isSuggestions()
    {
        return suggestions;
    }

    public void setSuggestions(boolean suggestions)
    {
        this.suggestions = suggestions;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.getDefault(), "id=%d - name=%s - icon=%d - publicList=%s - suggestions=%s - count=%d", id, name, iconResourceID, publicList, suggestions, count);
    }
}
