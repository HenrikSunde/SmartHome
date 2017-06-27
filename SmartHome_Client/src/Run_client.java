import constant.Filepath;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import security.CAServerConnection;

import java.io.File;
import java.security.Security;

public class Run_client
{
    public static void main(String[] args)
    {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        
        new File(Filepath.LOG_DIR).mkdirs();
        new File(Filepath.SECURITY_DIR).mkdirs();
        
        CAServerConnection caServerConnection = new CAServerConnection();
        caServerConnection.start();
        try
        {
            caServerConnection.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
