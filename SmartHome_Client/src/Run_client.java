import constant.Filepath;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import security.CACertificateServerConnection;
import security.CAServerConnection;
import util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

public class Run_client
{
    private static LogUtil log = new LogUtil("Run_client Main class");

    public static void main(String[] args)
    {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        
        new File(Filepath.LOG_DIR).mkdirs();
        new File(Filepath.SECURITY_DIR).mkdirs();

        File keystoreFile = new File(Filepath.KEYSTORE);
        String keystorePassword = "123456";

        if (keystoreFile.exists())
        {
            log.i("A KeyStore already exists");
        }
        else
        {
            log.i("Creating new KeyStore...");

            try
            {
                KeyStore keyStore = setUpKeystore(keystorePassword);

                // Connect to the CA server explicitly for receiving its root certificate
                CACertificateServerConnection certificateServerConnection = new CACertificateServerConnection("SmartHomeClient", keystorePassword, keyStore);
                certificateServerConnection.start();
                certificateServerConnection.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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

    private static KeyStore setUpKeystore(String keystorePassword) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException
    {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, keystorePassword.toCharArray());


        return keyStore;
    }
}
