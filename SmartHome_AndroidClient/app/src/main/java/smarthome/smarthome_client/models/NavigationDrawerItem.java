package smarthome.smarthome_client.models;


/***************************************************************************************************
 *
 **************************************************************************************************/
public class NavigationDrawerItem
{
    private String itemName;
    private int iconResourceID;
    private boolean publicList;

    public NavigationDrawerItem(String itemName, int iconResourceID, boolean publicList)
    {
        this.itemName = itemName;
        this.iconResourceID = iconResourceID;
        this.publicList = publicList;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
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
}
