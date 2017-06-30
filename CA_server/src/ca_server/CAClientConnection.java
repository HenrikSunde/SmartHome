package ca_server;

import callback.CAServerControllerCallback;
import javafx.application.Platform;
import util.LogUtil;
import util.SocketReaderUtil;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    // The latch is for the GUI controller to be able to notify this class to continue its process.
    public final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Constructor
     * */
    public CAClientConnection(SSLSocket connection, CAServerControllerCallback callback)
    {
        log = new LogUtil(TAG);
        connectTime = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(new Date());
        this.connection = connection;
        this.callback = callback;
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
            connection.setNeedClientAuth(false); // The client should have the CA's certificate.
            connection.setEnabledCipherSuites(connection.getEnabledCipherSuites());
            connection.startHandshake();
    
            connectionIn = new DataInputStream(connection.getInputStream());
            connectionOut = new DataOutputStream(connection.getOutputStream());
    

            String clientID = SocketReaderUtil.readString(connectionIn);
            log("Received clientID = " + clientID);

            // Notify the GUI that a client has connected and desires to receive a signed certificate.
            Platform.runLater(() -> callback.clientConnected(clientID, connectTime, latch));
            
            latch.await();
            
            //This code will be reached when latch.countDown() is called from anywhere:
            //Code...
            System.out.println("CSR signing in process...");
        }
        catch (Exception e) {}
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
