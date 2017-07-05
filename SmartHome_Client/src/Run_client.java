import constant.Filepath;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import security.CACertificateServerConnection;
import security.CAServerConnection;
import util.CryptographyGenerator;
import util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
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

        String host_ip = "localhost";

        if (keystoreFile.exists())
        {
            log.i("A KeyStore already exists");
        }
        else
        {
            log.i("Creating new KeyStore...");

            try
            {
                // Connect to the CA server explicitly for receiving its root certificate
                CACertificateServerConnection certificateServerConnection = new CACertificateServerConnection(keystorePassword, host_ip);
                certificateServerConnection.start();
                certificateServerConnection.join();

                // Connect to the CA server explicitly to send a CSR and receive a signed certificate
                CAServerConnection caServerConnection = new CAServerConnection(keystorePassword, host_ip);
                caServerConnection.start();
                caServerConnection.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
