package smarthome.smarthome_client.comparators;

import java.util.Comparator;

import smarthome.smarthome_client.models.ItemlistItem;
import smarthome.smarthome_client.models.ItemlistTitleItem;
import smarthome.smarthome_client.models.Suggestion;
import smarthome.smarthome_client.models.interfaces.NameableItem;

import static smarthome.smarthome_client.logic.Logic.dateFormatToDate;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ComparatorFactory
{
    /**
     * ItemlistTitleItems
     * */
    public Comparator<ItemlistTitleItem> getTitleItemNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<ItemlistTitleItem>()
            {
                @Override
                public int compare(ItemlistTitleItem o1, ItemlistTitleItem o2)
                {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
        else
        {
            return new Comparator<ItemlistTitleItem>()
            {
                @Override
                public int compare(ItemlistTitleItem o1, ItemlistTitleItem o2)
                {
                    return o2.getName().compareTo(o1.getName());
                }
            };
        }
    }

    /**
     * ItemlistItems
     * */
    public Comparator<ItemlistItem> getItemNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<ItemlistItem>()
            {
                @Override
                public int compare(ItemlistItem o1, ItemlistItem o2)
                {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
        else
        {
            return new Comparator<ItemlistItem>()
            {
                @Override
                public int compare(ItemlistItem o1, ItemlistItem o2)
                {
                    return o2.getName().compareTo(o1.getName());
                }
            };
        }
    }
    public Comparator<ItemlistItem> getDateComparator(boolean newLast)
    {
        if (newLast)
        {
            return new Comparator<ItemlistItem>()
            {
                @Override
                public int compare(ItemlistItem o1, ItemlistItem o2)
                {
                    return dateFormatToDate(o1.getDateAddedFormatted()).compareTo(dateFormatToDate(o2.getDateAddedFormatted()));
                }
            };
        }
        return new Comparator<ItemlistItem>()
        {
            @Override
            public int compare(ItemlistItem o1, ItemlistItem o2)
            {
                return dateFormatToDate(o2.getDateAddedFormatted()).compareTo(dateFormatToDate(o1.getDateAddedFormatted()));
            }
        };
    }

    /**
     * Suggestions
     * */
    public Comparator<Suggestion> getSuggestionNameComparator(boolean ascending)
    {
        if (ascending)
        {
            return new Comparator<Suggestion>()
            {
                @Override
                public int compare(Suggestion o1, Suggestion o2)
                {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
        else
        {
            return new Comparator<Suggestion>()
            {
                @Override
                public int compare(Suggestion o1, Suggestion o2)
                {
                    return o2.getName().compareTo(o1.getName());
                }
            };
        }
    }
}
