package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 */
public class FileReaderUtil
{
    /**
     * Read all bytes from a file.
     *
     * @param location The file to read from.
     * @return The read bytes in an array of bytes if reading was successful, false otherwise.<p>Null if file is larger
     * than 2GB (larger than Integer.MAX_VALUE bytes).
     */
    public static byte[] readBytes(File location)
    {
        if (location.length() > Integer.MAX_VALUE)
        {
             /* Files larger than ~2GB are not supported by this method. */
            return null;
        }
        byte[] bytes = new byte[(int) location.length()];
        try
        {
            FileInputStream in = new FileInputStream(location);
            int count = in.read(bytes);
            if (count != location.length())
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
}
