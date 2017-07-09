package smarthome.smarthome_client.util;

import java.io.File;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class FileUtil
{
    public static void delete(File... files)
    {
        for (File file : files)
        {
            file.delete();
        }
    }
}
