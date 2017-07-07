package ca_server;

import callback.CAServerControllerCallback;
import constant.Filepath;
import constant.SysProp;
import javafx.application.Platform;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import util.*;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Communicates using TLS with clients that sends CSRs to this server.
 * This class should receive the CSR, sign the certificate with the CA's private key
 * and send the signed certificate back to the client.
 * */
public class CAClientConnection extends Thread
{
    private SSLSocket connection;
    private DataInputStream connectionIn;
    private DataOutputStream connectionOut;
    
    private CAServerControllerCallback callback;
    private String connectTime;
    private final String TAG = getClass().getSimpleName();
    private LogUtil log;
    private String keystorePassword;

    // The latch is for the GUI controller to be able to notify this class to continue its process.
    public final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Constructor
     * */
    public CAClientConnection(SSLSocket connection, CAServerControllerCallback callback, String keystorePassword)
    {
        log = new LogUtil(TAG);
        connectTime = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(new Date());
        this.connection = connection;
        this.callback = callback;
        this.keystorePassword = keystorePassword;
    }

    /**
     * This is where communication with the client takes place.
     * The method should sign and send back the client's certificate.
     * */
    @Override
    public void run()
    {
        try
        {
            connection.setUseClientMode(false);
            connection.setNeedClientAuth(false);
            connection.setEnabledCipherSuites(connection.getSupportedCipherSuites());
            connection.startHandshake();
    
            connectionIn = new DataInputStream(connection.getInputStream());
            connectionOut = new DataOutputStream(connection.getOutputStream());
    

            String clientID = SocketReaderUtil.readString(connectionIn);
            log.i("Received clientID = " + clientID);

            String csrString = SocketReaderUtil.readString(connectionIn);
            JcaPKCS10CertificationRequest csr = (JcaPKCS10CertificationRequest) CryptographyGenerator.stringToPemObject(csrString);
            log.i("Received csr from " + clientID);

            // Notify the GUI that a client has connected and desires to receive a signed certificate.
            Platform.runLater(() -> callback.clientConnected(clientID, connectTime, latch));
            latch.await();
            
            // This code will be reached when latch.countDown() is called from anywhere:
            FileInputStream keyStoreIn = new FileInputStream(Filepath.KEYSTORE);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStoreIn, keystorePassword.toCharArray());

            // Retrieve the private key and the root certificate from the keystore
            PrivateKey privateKey = (PrivateKey) keyStore.getKey("SmartHomePK", keystorePassword.toCharArray());
            X509Certificate rootCert = (X509Certificate) keyStore.getCertificate("SmartHomeCA");

            // Sign the CSR and send the signed certificate to the client
            X509Certificate signedCert = CryptographyGenerator.signCSR(privateKey, rootCert, csr);
            String signedCertString = CryptographyGenerator.pemObjectToString(signedCert);
            SocketWriterUtil.writeString(signedCertString, connectionOut);
        }
        catch (Exception e)
        {
            log("Exception caught. Message: " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            CloseableUtil.close(connection);
        }
    }

    /**
     * Desired method of logging.
     * */
    private void log(String logMessage)
    {
        log.i(logMessage);
        Platform.runLater(() -> callback.addLogMessage(logMessage));
    }
}
