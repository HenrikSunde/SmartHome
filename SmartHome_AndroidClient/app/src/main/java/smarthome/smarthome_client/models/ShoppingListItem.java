package smarthome.smarthome_client.models;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppingListItem
{
    private String itemName;
    private boolean marked;

    public ShoppingListItem(String itemName)
    {
        this.itemName = itemName;
        marked = false;
    }

    public ShoppingListItem(String itemName, boolean marked)
    {
        this.itemName = itemName;
        this.marked = marked;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public boolean isMarked()
    {
        return marked;
    }

    public void setMarked(boolean marked)
    {
        this.marked = marked;
    }

    @Override
    public String toString()
    {
        return itemName + ", marked=" + marked;
    }
}
