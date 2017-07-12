package util;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtil
{
    /**
     * Closes the closeable objects that are passed as parameters.
     *
     * @param closeables The closeable objects that should be closed.
     * @return True if all closeables passed as parameters successfully closed, false otherwise.
     */
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
