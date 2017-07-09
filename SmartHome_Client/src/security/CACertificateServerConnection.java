package security;

import constant.Filepath;
import util.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;

/**
 * This Thread will connect to the CA server to request its root certificate.
 * The certificate will later be used to establish a TLS connection to the CA server to exchange CSR/signed certificate
 * */
public class CACertificateServerConnection extends Thread
{
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    private String keystorePassword;
    private String host_ip;
    private final int PORT = 24575;

    private Socket connection;
    private DataInputStream connectionIn;

    /**
     * Constructor
     * */
    public CACertificateServerConnection(String keystorePassword, String host_ip)
    {
        this.keystorePassword = keystorePassword;
        this.host_ip = host_ip;
    }

    /**
     * Set up a keystore to store certificates.
     * Connect to the CA server, receive its root certificate and import it to the keystore.
     * */
    @Override
    public void run()
    {
        try
        {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, keystorePassword.toCharArray());

            log.i("Connecting to the CA to receive its root certificate...");
            connection = new Socket(host_ip, PORT);
            connectionIn = new DataInputStream(connection.getInputStream());

            log.i("Receiving root certificate...");
            String rootCertString = SocketReaderUtil.readString(connectionIn);

            log.i("Importing root certificate to keystore...");
            X509Certificate rootCert = (X509Certificate) CryptographyGenerator.stringToPemObject(rootCertString);
            keyStore.setCertificateEntry("SmartHomeCA", rootCert);

            log.i("Storing the keystore...");
            FileOutputStream keystoreOut = new FileOutputStream(Filepath.KEYSTORE);
            keyStore.store(keystoreOut, keystorePassword.toCharArray());
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            log.i("Closing data streams to the CA...");
            CloseableUtil.close(connection);
        }
    }
}
