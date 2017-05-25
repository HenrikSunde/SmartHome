package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 */
public class FileToSocketStreamUtil
{
    static final int EOF = -5;
    private static final int STREAM_SIZE = 1024 * 4;
    
    /**
     * Stream a File to a DataOutputStream.
     *
     * @param location The File to stream.
     * @param out      The DataOutputStream to stream to.
     * @return True if the file was streamed successfully, false otherwise.
     */
    public static boolean doStream(File location, DataOutputStream out)
    {
        try
        {
            FileInputStream in = new FileInputStream(location);
            
            
            long bytesLeft = location.length();
            while (bytesLeft > 0)
            {
                int length = STREAM_SIZE;
                if (bytesLeft < length)
                {
                    length = (int) bytesLeft;
                }
                byte[] bytes = FileReaderUtil.readBytes(in, length);
                SocketWriterUtil.writeBytes(bytes, out);
                
                bytesLeft -= length;
            }
            /* Write -5 so that the receiver can know that the end of the file has been reached */
            SocketWriterUtil.writeInt(EOF, out);
            
            CloseableUtil.close(in);
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        return true;
    }
}
