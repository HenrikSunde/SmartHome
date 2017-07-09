package smarthome.smarthome_client.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.Charset;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class FileReaderUtil
{
    /**
     * Read all bytes from a file.
     *
     * @param location The file to read from.
     * @return The read bytes in an array of bytes if reading was successful, false otherwise.<p>Null if file is larger
     * than 2GB (larger than Integer.MAX_VALUE bytes).
     */
    public static byte[] readBytes(String location, Context context)
    {
        File locationFile = new File(context.getFilesDir(), location);
        if (locationFile.length() > Integer.MAX_VALUE)
        {
             /* Files larger than ~2GB are not supported by this method. */
            return null;
        }
        byte[] bytes = new byte[(int) location.length()];
        try
        {
            FileInputStream in = context.openFileInput(location);
            int count = in.read(bytes);
            if (count != locationFile.length())
            {
                return null;
            }
            if (!CloseableUtil.close(in))
            {
                return null;
            }
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        return bytes;
    }

    /**
     * Read length bytes, starting at byte number offset, from FileInputStream.
     *
     * @param in     The FileInputStream to read from.
     * @param length The number of bytes to read.
     * @return The read bytes in an array of bytes if reading was successful, false otherwise.
     */
    static byte[] readBytes(FileInputStream in, int length)
    {
        byte[] bytes = new byte[length];
        try
        {
            int count = in.read(bytes, 0, length);
            if (count != length)
            {
                return null;
            }
        }
        catch (IOException e)
        {
            return null;
        }
        return bytes;
    }

    public static String readString(String location, Context context)
    {
        return new String(readBytes(location, context), Charset.forName("UTF-8"));
    }
}
