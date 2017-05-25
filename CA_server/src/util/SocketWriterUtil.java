package util;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 */
public class SocketWriterUtil
{
    /**
     * Writes an int to a DataOutputStream.
     *
     * @param i   The int to write.
     * @param out The DataOutputStream to write to.
     * @return True if the int is written successfully
     */
    public static boolean writeInt(int i, DataOutputStream out)
    {
        try
        {
            out.writeInt(i);
            out.flush();
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
    
    /**
     * Writes an array of bytes to a DataOutputStream.
     *
     * @param bytes The bytes to write.
     * @param out   The DataOutputStream to write to.
     * @return True if the bytes are written successfully.
     */
    public static boolean writeBytes(byte[] bytes, DataOutputStream out)
    {
        try
        {
            if (!writeInt(bytes.length, out))
            {
                return false;
            }
            out.write(bytes);
            out.flush();
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
}
