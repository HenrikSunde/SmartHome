package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Class for writing data to a file
 */
public class FileWriterUtil
{
    /**
     * Writes an array of bytes to a file. The resulting file contains nothing but these bytes.
     *
     * @param bytes       The bytes to write.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the bytes was written to the file successfully, false otherwise.
     */
    public static boolean writeBytes(byte[] bytes, File destination)
    {
        return doBytes(bytes, destination, false);
    }
    
    /**
     * Appends an array of bytes to the end of a file.
     *
     * @param bytes       The bytes to append.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the bytes was appended to the file successfully, false otherwise.
     */
    public static boolean appendBytes(byte[] bytes, File destination)
    {
        return doBytes(bytes, destination, true);
    }
    
    /**
     *
     */
    private static boolean doBytes(byte[] bytes, File destination, boolean append)
    {
        try
        {
            FileOutputStream out = new FileOutputStream(destination, append);
            out.write(bytes);
            out.flush();
            
            if (!CloseableUtil.close(out))
            {
                return false;
            }
        }
        catch (Exception e)
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
    public static boolean writeString(String text, boolean newLineOnEnd, File destination)
    {
        return doString(text, destination, false, newLineOnEnd);
    }
    
    /**
     * Appends a string to the end of a file. A new line will automatically be inserted at the end.
     *
     * @param text        The string to append.
     * @param destination The destination file that should be written to, it will be made if it doesn't exist.
     * @return True if the string was appended to the file successfully, false otherwise.
     */
    public static boolean appendString(String text, boolean newLineOnEnd, File destination)
    {
        return doString(text, destination, true, newLineOnEnd);
    }
    
    /**
     *
     */
    private static boolean doString(String text, File destination, boolean append, boolean newLineOnEnd)
    {
        try
        {
            FileOutputStream out = new FileOutputStream(destination, append);
            
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
