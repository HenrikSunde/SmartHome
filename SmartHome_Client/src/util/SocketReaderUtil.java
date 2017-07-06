package util;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 */
public class SocketReaderUtil
{
    /**
     * Reads an int from a DataInputStream.
     *
     * @param in The DataInputStream to read from.
     * @return The read int.<p>-2 if something went wrong.
     */
    public static int readInt(DataInputStream in)
    {
        int i;
        try
        {
            i = in.readInt();
        }
        catch (IOException e)
        {
            return -2;
        }
        return i;
    }
    
    /**
     * Read an array of bytes from a DataInputStream.
     *
     * @param in The DataInputStream to read from.
     * @return The read array of bytes.<p>Null if something went wrong.
     */
    public static byte[] readBytes(DataInputStream in)
    {
        int i = readInt(in);
        return readBytes(in, i);
    }
    
    /**
     *
     */
    public static byte[] readBytes(DataInputStream in, int length)
    {
        byte[] bytes;
        try
        {
            bytes = new byte[length];
            int count = in.read(bytes);
            if (count != length)
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return bytes;
    }
    
    public static String readString(DataInputStream in)
    {
        return new String(readBytes(in), Charset.forName("UTF-8"));
    }
}
