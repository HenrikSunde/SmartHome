package smarthome.smarthome_client.comparators;

import java.util.Comparator;

import smarthome.smarthome_client.models.ItemlistItem;

import static smarthome.smarthome_client.models.ItemlistItem.dateFormatToDate;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemlistItem_Comparator
{
    public static Comparator<ItemlistItem> getNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<ItemlistItem>()
            {
                @Override
                public int compare(ItemlistItem o1, ItemlistItem o2)
                {
                    return o1.getItemName().compareTo(o2.getItemName());
                }
            };
        }
        return new Comparator<ItemlistItem>()
        {
            @Override
            public int compare(ItemlistItem o1, ItemlistItem o2)
            {
                return o2.getItemName().compareTo(o1.getItemName());
            }
        };
    }

    public static Comparator<ItemlistItem> getDateComparator(boolean newLast)
    {
        if (newLast)
        {
            return new Comparator<ItemlistItem>()
            {
                @Override
                public int compare(ItemlistItem o1, ItemlistItem o2)
                {
                    return dateFormatToDate(o1.getAddDateFormatted()).compareTo(dateFormatToDate(o2.getAddDateFormatted()));
                }
            };
        }
        return new Comparator<ItemlistItem>()
        {
            @Override
            public int compare(ItemlistItem o1, ItemlistItem o2)
            {
                return dateFormatToDate(o2.getAddDateFormatted()).compareTo(dateFormatToDate(o1.getAddDateFormatted()));
            }
        };
    }
}
