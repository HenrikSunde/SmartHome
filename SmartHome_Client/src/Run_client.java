import constant.Filepath;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import security.CACertificateServerConnection;
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

        // Connect to the CA server explicitly for receiving its root certificate
        CACertificateServerConnection certificateServerConnection = new CACertificateServerConnection("SmartHomeClient1");
        certificateServerConnection.start();
        try
        {
            certificateServerConnection.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // Connect to the CA server explicitly to send a CSR and receive a signed certificate
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
