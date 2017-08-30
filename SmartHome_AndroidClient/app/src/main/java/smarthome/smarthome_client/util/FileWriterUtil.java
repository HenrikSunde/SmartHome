package smarthome.smarthome_client.util;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class FileWriterUtil
{
    /**
     * Writes an array of bytes to a file. The resulting file contains nothing but these bytes.
     *
     * @param bytes       The bytes to write.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the bytes was written to the file successfully, false otherwise.
     */
    public static boolean writeBytes(byte[] bytes, String destination, Context context)
    {
        return doBytes(bytes, destination, context, false);
    }

    /**
     * Appends an array of bytes to the end of a file.
     *
     * @param bytes       The bytes to append.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the bytes was appended to the file successfully, false otherwise.
     */
    public static boolean appendBytes(byte[] bytes, String destination, Context context)
    {
        return doBytes(bytes, destination, context, true);
    }

    /**
     *
     */
    private static boolean doBytes(byte[] bytes, String destination, Context context, boolean append)
    {
        try
        {
            FileOutputStream out;
            if (append)
            {
                out = context.openFileOutput(destination, Context.MODE_APPEND);
            }
            else
            {
                out = context.openFileOutput(destination, Context.MODE_PRIVATE);
            }
            out.write(bytes);
            out.flush();

            if (!CloseableUtil.close(out))
            {
                return false;
            }
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    /**
     * Writes an array of bytes with a FileOutputStream. The OutputStream should be closed after streaming all the bytes
     *
     * @param bytes The bytes to write to the FileOutputStream.
     * @param out   The FileOutputStream for the file that should be written to.
     * @return True if the bytes was written to the FileOutputStream successfully, false otherwise.
     */
    static boolean streamBytes(byte[] bytes, FileOutputStream out)
    {
        try
        {
            out.write(bytes);
            out.flush();
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Writes a string to a file. The resulting file contains nothing but this string and a new line that will
     * automatically be inserted at the end.
     *
     * @param text        The string to write.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the string was written to the file successfully, false otherwise.
     */
    public static boolean writeString(String text, boolean newLineOnEnd, String destination, Context context)
    {
        return doString(text, destination, context, false, newLineOnEnd);
    }

    /**
     * Appends a string to the end of a file. A new line will automatically be inserted at the end.
     *
     * @param text        The string to append.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the string was appended to the file successfully, false otherwise.
     */
    public static boolean appendString(String text, boolean newLineOnEnd, String destination, Context context)
    {
        return doString(text, destination, context, true, newLineOnEnd);
    }

    /**
     *
     */
    private static boolean doString(String text, String destination, Context context, boolean append, boolean newLineOnEnd)
    {
        try
        {
            FileOutputStream out;
            if (append)
            {
                out = context.openFileOutput(destination, Context.MODE_APPEND);
            }
            else
            {
                out = context.openFileOutput(destination, Context.MODE_PRIVATE);
            }

            if (newLineOnEnd)
            {
                text += System.getProperty("line.separator");
            }
            out.write(text.getBytes(Charset.forName("UTF-8")));
            out.flush();

            if (!CloseableUtil.close(out))
            {
                return false;
            }
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }
}
