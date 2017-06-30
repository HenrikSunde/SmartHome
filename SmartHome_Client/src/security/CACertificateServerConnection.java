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
    private File keystoreFile = new File(Filepath.KEYSTORE);
    private File rootCertFile = new File(Filepath.ROOT_CERT);
    private KeyStore keyStore;
    private final String HOST_IP = "localhost";
    private final int PORT = 24575;

    // Temporary set values
    private String keystorePassword;
    private int keySize = 2048;
    private String CN = "SmartHome";
    private int validYears = 25;
    private String alias;

    /**
     * Constructor
     * */
    public CACertificateServerConnection(String alias, String keystorePassword, KeyStore keyStore)
    {
        this.alias = alias;
        this.keystorePassword = keystorePassword;
        this.keyStore = keyStore;
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
            log.i("Connecting to the CA to receive its root certificate...");
            Socket socket = new Socket(HOST_IP, PORT);
            DataInputStream socketIn = new DataInputStream(socket.getInputStream());

            log.i("Receiving root certificate...");
            SocketToFileStreamUtil.doStream(socketIn, rootCertFile);

            log.i("Closing data streams to the CA...");
            CloseableUtil.close(socketIn, socket);

            keyStore.setCertificateEntry("SmartHomeCA", CryptographyGenerator.stringToCertificate(FileReaderUtil.readString(rootCertFile)));

            FileOutputStream keystoreOut = new FileOutputStream(keystoreFile);
            keyStore.store(keystoreOut, keystorePassword.toCharArray());
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
        }
    }

    private void setUpKeystore() throws NoSuchProviderException, NoSuchAlgorithmException
    {
        log.i("Generating RSA keypair...");
        KeyPair keyPair = CryptographyGenerator.generateRSAKeyPair();

        /* TODO: This might not be necessary
        log.i("Generating self-signed certificate...");
        X509Certificate selfSignedCert = CryptographyGenerator.generateSelfSignedCert(CN, validYears, keyPair);

        log.i("Storing the self-signed certificate in the KeyStore...");
        keyStore.setCertificateEntry(alias, selfSignedCert);
        */
    }
}
