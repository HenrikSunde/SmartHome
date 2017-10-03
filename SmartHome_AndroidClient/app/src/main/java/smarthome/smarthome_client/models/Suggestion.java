package smarthome.smarthome_client.models;

import java.util.Locale;

import smarthome.smarthome_client.models.interfaces.NameableItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Suggestion implements NameableItem
{
    private int id;
    private String name;
    private int list_id;

    public Suggestion(String name, int list_id)
    {
        this.name = name;
        this.list_id = list_id;
    }

    public Suggestion(int id, String name, int list_id)
    {
        this(name, list_id);
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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
        return String.format(Locale.getDefault(), "id=%d - name=%s - list_id=%d", id, name, list_id);
    }
}
