package smarthome.smarthome_client.comparators;

import java.util.Comparator;

import smarthome.smarthome_client.models.ShoppingListItem;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ItemSuggestionComparator
{
    public static Comparator<String> getNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<String>()
            {
                @Override
                public int compare(String o1, String o2)
                {
                    return o1.compareTo(o2);
                }
            };
        }
        return new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                return o2.compareTo(o1);
            }
        };
    }
}
