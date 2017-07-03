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
    private File rootCertFile = new File(Filepath.ROOT_CERT);
    private String host_ip;
    private final int PORT = 24575;

    private Socket connection;
    private DataInputStream connectionIn;

    // Temporary set values
    private String keystorePassword;
    private int keySize = 2048;
    private String CN = "SmartHome";
    private int validYears = 25;

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
            FileInputStream keystoreIn = new FileInputStream(Filepath.KEYSTORE);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keystoreIn, keystorePassword.toCharArray());

            log.i("Connecting to the CA to receive its root certificate...");
            connection = new Socket(host_ip, PORT);
            connectionIn = new DataInputStream(connection.getInputStream());

            log.i("Receiving root certificate...");
            SocketToFileStreamUtil.doStream(connectionIn, rootCertFile);

            log.i("Importing root certificate to keystore...");
            keyStore.setCertificateEntry("SmartHomeCA", CryptographyGenerator.stringToCertificate(FileReaderUtil.readString(rootCertFile)));

            FileOutputStream keystoreOut = new FileOutputStream(Filepath.KEYSTORE);
            keyStore.store(keystoreOut, keystorePassword.toCharArray());

            FileUtil.delete(rootCertFile);
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
        }
        finally
        {
            log.i("Closing data streams to the CA...");
            CloseableUtil.close(connection);
        }
    }
}
