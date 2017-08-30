package smarthome.smarthome_client.util;

import android.content.Context;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class SocketToFileStreamUtil
{
    /**
     * Stream from a DataInputStream to a File.
     *
     * @param in          The DataInputStream to stream from.
     * @param destination The File to stream to.
     * @return True if the file was streamed successfully, false otherwise.
     */
    public static boolean doStream(DataInputStream in, String destination, Context context)
    {
        try
        {
            FileOutputStream out = context.openFileOutput(destination, Context.MODE_PRIVATE);

            int length;
            while ((length = SocketReaderUtil.readInt(in)) != FileToSocketStreamUtil.EOF)
            {
                byte[] bytes = SocketReaderUtil.readBytes(in, length);
                FileWriterUtil.streamBytes(bytes, out);
            }
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        return true;
    }
}
