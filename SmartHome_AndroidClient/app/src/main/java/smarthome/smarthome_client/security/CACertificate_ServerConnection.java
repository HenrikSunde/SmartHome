package smarthome.smarthome_client.security;

import android.content.Context;
import android.util.Log;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.util.CloseableUtil;
import smarthome.smarthome_client.util.CryptographyGenerator;
import smarthome.smarthome_client.util.SocketReaderUtil;

/**
 * This Thread will connect to the CA server to request its root certificate.
 * The certificate will later be used to establish a TLS connection to the CA server to exchange CSR/signed certificate
 */
public class CACertificate_ServerConnection extends Thread
{
    private final int PORT = 24575;
    private final String TAG = getClass().getSimpleName();
    private String keystorePassword;
    private String host_ip;
    private Context context;

    private Socket connection;
    private DataInputStream connectionIn;

    /**
     * Constructor
     */
    public CACertificate_ServerConnection(String keystorePassword, String host_ip, Context context)
    {
        this.keystorePassword = keystorePassword;
        this.host_ip = host_ip;
        this.context = context;

        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * Set up a keystore to store certificates.
     * Connect to the CA server, receive its root certificate and import it to the keystore.
     */
    @Override
    public void run()
    {
        try
        {
            Log.i(TAG, "Connecting to the CA to receive its root certificate...");
            connection = new Socket(host_ip, PORT);
            connectionIn = new DataInputStream(connection.getInputStream());

            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(null, keystorePassword.toCharArray());

            Log.i(TAG, "Receiving root certificate...");
            String rootCertString = SocketReaderUtil.readString(connectionIn);

            Log.i(TAG, "Importing root certificate to keystore...");
            X509Certificate rootCert = (X509Certificate) CryptographyGenerator.stringToPemObject(rootCertString);
            keyStore.setCertificateEntry("SmartHomeCA", rootCert);

            Log.i(TAG, "Storing the keystore...");
            FileOutputStream keystoreOut = context.openFileOutput(context.getString(R.string.keystore_filename), Context.MODE_PRIVATE);
            keyStore.store(keystoreOut, keystorePassword.toCharArray());
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception caught. Message: " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            Log.i(TAG, "Closing data streams to the CA...");
            CloseableUtil.close(connection);
        }
    }
}
