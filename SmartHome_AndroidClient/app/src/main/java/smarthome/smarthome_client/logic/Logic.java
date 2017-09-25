package smarthome.smarthome_client.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Logic
{
    private Logic(){}


    public static String dateToDateFormat(Date date)
    {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(date);
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

    public static boolean charIsLowerCase(char character)
    {
        return (int) character >= 97 && (int) character <= 122;
    }

    public static boolean charIsUpperCase(char character)
    {
        return (int) character >= 65 && (int) character <= 90;
    }
}
