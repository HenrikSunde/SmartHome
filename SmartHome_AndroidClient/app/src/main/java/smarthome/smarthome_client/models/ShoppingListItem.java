package smarthome.smarthome_client.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class ShoppingListItem
{
    private String itemName;
    private boolean marked;
    private String dateAddedFormatted;
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);

    public ShoppingListItem(String itemName)
    {
        this(itemName, false);
    }

    public ShoppingListItem(String itemName, boolean marked)
    {
        this(itemName, marked, new Date());
    }

    public ShoppingListItem(String itemName, boolean marked, Date addDate)
    {
        this.itemName = itemName;
        this.marked = marked;
        dateAddedFormatted = dateToDateFormat(addDate);
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

    public String getAddDateFormatted()
    {
        return dateAddedFormatted;
    }

    public void setAddDateFormatted(String dateAdded)
    {
        this.dateAddedFormatted = dateAdded;
    }

    @Override
    public String toString()
    {
        return itemName + ", marked=" + marked + " - added=" + dateAddedFormatted;
    }

    public static Date dateFormatToDate(String dateFormatted)
    {
        Date returnDate = null;
        if (dateFormatted.length() == 14)
        {
            Calendar calendar = Calendar.getInstance();
            int year = Integer.parseInt(dateFormatted.substring(0, 4));
            int month = Integer.parseInt(dateFormatted.substring(4, 6)) - 1;
            int date = Integer.parseInt(dateFormatted.substring(6, 8));
            int hour = Integer.parseInt(dateFormatted.substring(8, 10));
            int minute = Integer.parseInt(dateFormatted.substring(10, 12));
            int second = Integer.parseInt(dateFormatted.substring(12, 14));
            calendar.set(year, month, date, hour, minute, second);
            returnDate = calendar.getTime();
        }
        return returnDate;
        // DATEFORMAT.format(returnDate) equals dateFormatted
    }

    public static String dateToDateFormat(Date date)
    {
        return DATEFORMAT.format(date);
    }
}
