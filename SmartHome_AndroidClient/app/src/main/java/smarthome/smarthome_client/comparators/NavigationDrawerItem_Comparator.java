package smarthome.smarthome_client.comparators;

import java.util.Comparator;

import smarthome.smarthome_client.models.NavigationDrawerItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class NavigationDrawerItem_Comparator
{
    public static Comparator<NavigationDrawerItem> getNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<NavigationDrawerItem>()
            {
                @Override
                public int compare(NavigationDrawerItem o1, NavigationDrawerItem o2)
                {
                    return o1.getItemName().compareTo(o2.getItemName());
                }
            };
        }
        return new Comparator<NavigationDrawerItem>()
        {
            @Override
            public int compare(NavigationDrawerItem o1, NavigationDrawerItem o2)
            {
                return o2.getItemName().compareTo(o1.getItemName());
            }
        };
    }
}
