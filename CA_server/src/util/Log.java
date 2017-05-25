package util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class Log
{
    private final File logFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "log");
    private String tag;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss.SSS");
    
    /**
     * Initializes this Log object with the specified tag.
     *
     * @param tag This object will log all messages with this tag.
     */
    public Log(String tag)
    {
        this.tag = tag;
    }
    
    /**
     * Appends a log message to the log file.
     *
     * @param message The message that should be logged.
     */
    public void i(String message)
    {
        String time = timeFormat.format(new Date());
        String logMessage = time + "   " + tag + " - " + message;
        FileWriterUtil.appendString(logMessage, logFile);
    }
}
