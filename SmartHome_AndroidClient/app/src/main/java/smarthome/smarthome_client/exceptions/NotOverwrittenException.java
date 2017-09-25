package smarthome.smarthome_client.exceptions;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class NotOverwrittenException extends UnsupportedOperationException
{
    public NotOverwrittenException(String message)
    {
        super(message);
    }

    public NotOverwrittenException()
    {
        super();
    }
}
