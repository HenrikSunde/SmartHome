package smarthome.smarthome_client.logic;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Logic
{
    public static boolean charIsLowerCase(char character)
    {
        return (int) character >= 97 && (int) character <= 122;
    }

    public static boolean charIsUpperCase(char character)
    {
        return (int) character >= 65 && (int) character <= 90;
    }
}
