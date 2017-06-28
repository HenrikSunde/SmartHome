package security;

import constant.Filepath;
import util.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/***********************************************************************************************************************
 *
 **********************************************************************************************************************/
public class CACertificateServerConnection extends Thread
{
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    private File keystoreFile = new File(Filepath.KEYSTORE);
    private File rootCertFile = new File(Filepath.ROOT_CERT);
    private final String HOST_IP = "localhost";
    private final int PORT = 24575;

    private String keystorePassword = "123456";
    private int keySize = 2048;
    private String CN = "SmartHome";
    private int validYears = 25;
    private String alias;

    public CACertificateServerConnection(String alias)
    {
        this.alias = alias;
    }

    @Override
    public void run()
    {
        if (keystoreFile.exists())
        {
            log.i("A KeyStore already exists");
        }
        else
        {
            log.i("Creating new KeyStore...");

            try
            {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(null, keystorePassword.toCharArray());

                log.i("Generating RSA keypair...");
                KeyPair keyPair = CryptographyGenerator.generateRSAKeyPair();

                log.i("Generating self-signed certificate...");
                X509Certificate selfSignedCert = CryptographyGenerator.generateSelfSignedCert(CN, validYears, keyPair);

                log.i("Storing the self-signed certificate in the KeyStore...");
                keyStore.setCertificateEntry(alias, selfSignedCert);

                //------------------------------------------------------------------

                log.i("Connecting to the CA to receive its root certificate...");
                Socket socket = new Socket(HOST_IP, PORT);
                DataInputStream socketIn = new DataInputStream(socket.getInputStream());

                log.i("Receiving root certificate...");
                SocketToFileStreamUtil.doStream(socketIn, rootCertFile);

                log.i("Closing data streams to the CA...");
                CloseableUtil.close(socketIn, socket);

                keyStore.setCertificateEntry("SmartHomeCA", CryptographyGenerator.stringToCertificate(FileReaderUtil.readString(rootCertFile)));

                //-------------------------------------------------------------------

                FileOutputStream keystoreOut = new FileOutputStream(keystoreFile);
                keyStore.store(keystoreOut, keystorePassword.toCharArray());
            }
            catch (Exception e)
            {
                log.i("Exception caught. Message: " + e.getMessage());
            }
        }
    }
}
