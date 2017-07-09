package smarthome.smarthome_client.security;

import android.content.Context;
import android.util.Log;

import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.util.CloseableUtil;
import smarthome.smarthome_client.util.CryptographyGenerator;
import smarthome.smarthome_client.util.FileToSocketStreamUtil;
import smarthome.smarthome_client.util.SocketReaderUtil;
import smarthome.smarthome_client.util.SocketToFileStreamUtil;
import smarthome.smarthome_client.util.SocketWriterUtil;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class CAServerConnection extends Thread
{
    private String TAG = getClass().getSimpleName();
    private String keystorePassword;
    private String host_ip;
    private final int PORT = 24576;

    private Context context;
    private SSLSocket connection;
    private DataInputStream connectionIn;
    private DataOutputStream connectionOut;

    public CAServerConnection(String keystorePassword, String host_ip, Context context)
    {
        this.keystorePassword = keystorePassword;
        this.host_ip = host_ip;
        this.context = context;
    }

    @Override
    public void run()
    {
        try
        {
            FileInputStream keystoreIn = context.openFileInput(context.getString(R.string.keystore_filename));
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keystoreIn, keystorePassword.toCharArray());
            CloseableUtil.close(keystoreIn);


            // Generate RSA keypair, a self signed certificate and a CSR for the CA to sign
            Log.i(TAG, "Generating RSA keypair...");
            KeyPair keyPair = CryptographyGenerator.generateRSAKeyPair();

            Log.i(TAG, "Generating self-signed certificate...");
            X509Certificate selfSigned = CryptographyGenerator.generateSelfSignedCert("SmartHomeClient", 50, keyPair);

            Log.i(TAG, "Importing the self-signed certificate to the keystore...");
            keyStore.setCertificateEntry("self_signed", selfSigned);


            // Initiate the SSL context
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            // Create a socket that is bound to the server
            Log.i(TAG, "Connecting to the CA to send a CSR and receive a signed certificate...");
            SSLSocketFactory sf = sslContext.getSocketFactory();
            connection = (SSLSocket) sf.createSocket(host_ip, PORT);
            connection.setUseClientMode(true);
            connection.setEnabledCipherSuites(connection.getSupportedCipherSuites());
            connection.startHandshake();

            // Get the socket input and output streams
            connectionIn = new DataInputStream(connection.getInputStream());
            connectionOut = new DataOutputStream(connection.getOutputStream());


            // Communicate with the server
            Log.i(TAG, "Sending my client ID to the server...");
            SocketWriterUtil.writeString("SmartHomeClient", connectionOut);

            Log.i(TAG, "Generating a csr...");
            JcaPKCS10CertificationRequest csr = CryptographyGenerator.generateCSR(keyPair, "SmartHomeClient");
            String csrString = CryptographyGenerator.pemObjectToString(csr);

            Log.i(TAG, "Sending csr to CA server...");
            SocketWriterUtil.writeString(csrString, connectionOut);

            Log.i(TAG, "Waiting for server to send back a signed certificate...");
            String signedCertString = SocketReaderUtil.readString(connectionIn);

            X509Certificate signedCert = (X509Certificate) CryptographyGenerator.stringToPemObject(signedCertString);
            X509Certificate rootCert = (X509Certificate) keyStore.getCertificate("SmartHomeCA");

            Log.i(TAG, "Importing the signed certificate to the KeyStore...");
            keyStore.setKeyEntry("SmartHomeClient", keyPair.getPrivate(), keystorePassword.toCharArray(), new Certificate[]{signedCert, rootCert});

            Log.i(TAG, "Saving the contents of the KeyStore...");
            FileOutputStream keystoreOut = context.openFileOutput(context.getString(R.string.keystore_filename), Context.MODE_PRIVATE);
            keyStore.store(keystoreOut, keystorePassword.toCharArray());

            // TESTING ONLY
            FileToSocketStreamUtil.doStream(context.getString(R.string.keystore_filename), context, connectionOut);
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception caught. Message: " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            CloseableUtil.close(connection);
        }
    }
}
