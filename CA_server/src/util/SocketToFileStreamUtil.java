package util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 */
public class SocketToFileStreamUtil
{
    /**
     * Stream from a DataInputStream to a File.
     *
     * @param in          The DataInputStream to stream from.
     * @param destination The File to stream to.
     * @return True if the file was streamed successfully, false otherwise.
     */
    public static boolean doStream(DataInputStream in, File destination)
    {
        try
        {
            FileOutputStream out = new FileOutputStream(destination);
            
            int length;
            while ((length = SocketReaderUtil.readInt(in)) != FileToSocketStreamUtil.EOF)
            {
                byte[] bytes = SocketReaderUtil.readBytes(in, length);
                FileWriterUtil.streamBytes(bytes, out);
            }
            CloseableUtil.close(out);
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        return true;
    }
}
