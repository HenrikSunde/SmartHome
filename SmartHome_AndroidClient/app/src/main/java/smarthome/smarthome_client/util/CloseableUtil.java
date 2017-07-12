package smarthome.smarthome_client.util;

import java.io.Closeable;
import java.io.IOException;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class CloseableUtil
{
    public static boolean close(Closeable... closeables)
    {
        for (Closeable closeable : closeables)
        {
            if (closeable != null)
            {
                try
                {
                    closeable.close();
                }
                catch (IOException e)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
