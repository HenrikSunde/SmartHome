package smarthome.smarthome_client.comparators;

import java.util.Comparator;

import static smarthome.smarthome_client.models.ShoppingListItem.*;
import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppinglistItemComparator
{
    public static Comparator<ShoppingListItem> getNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<ShoppingListItem>()
            {
                @Override
                public int compare(ShoppingListItem o1, ShoppingListItem o2)
                {
                    return o1.getItemName().compareTo(o2.getItemName());
                }
            };
        }
        return new Comparator<ShoppingListItem>()
        {
            @Override
            public int compare(ShoppingListItem o1, ShoppingListItem o2)
            {
                return o2.getItemName().compareTo(o1.getItemName());
            }
        };
    }

    public static Comparator<ShoppingListItem> getDateComparator(boolean newLast)
    {
        if (newLast)
        {
            return new Comparator<ShoppingListItem>()
            {
                @Override
                public int compare(ShoppingListItem o1, ShoppingListItem o2)
                {
                    return dateFormatToDate(o1.getAddDateFormatted()).compareTo(dateFormatToDate(o2.getAddDateFormatted()));
                }
            };
        }
        return new Comparator<ShoppingListItem>()
        {
            @Override
            public int compare(ShoppingListItem o1, ShoppingListItem o2)
            {
                return dateFormatToDate(o2.getAddDateFormatted()).compareTo(dateFormatToDate(o1.getAddDateFormatted()));
            }
        };
    }
}
