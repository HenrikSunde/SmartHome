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

public class ClientConnection extends Thread
{
    private SSLSocket connection;
    private DataInputStream connectionIn;
    private DataOutputStream connectionOut;
    
    private CAServerControllerCallback callback;
    private String connectTime;
    private final String TAG = getClass().getSimpleName();
    private LogUtil log;
    public final CountDownLatch latch = new CountDownLatch(1);
    
    public ClientConnection(SSLSocket connection, CAServerControllerCallback callback)
    {
        log = new LogUtil(TAG);
        connectTime = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss").format(new Date());
        this.connection = connection;
        this.callback = callback;
    }
    
    @Override
    public void run()
    {
        try
        {
            connection.setUseClientMode(false);
            connection.setNeedClientAuth(false);
            connection.setEnabledCipherSuites(connection.getEnabledCipherSuites());
            connection.startHandshake();
    
            connectionIn = new DataInputStream(connection.getInputStream());
            connectionOut = new DataOutputStream(connection.getOutputStream());
    
            //------------------------------------------------------------------------------------
            String clientID = SocketReaderUtil.readString(connectionIn);
            log("Received clientID = " + clientID);
    
            Platform.runLater(() -> callback.clientConnected(clientID, connectTime, latch));
            
            latch.await();
            
            //This code will be reached when latch.countDown() is called from anywhere:
            //Code...
            System.out.println("CSR signing in process...");
        }
        catch (Exception e) {}
    }
    
    private void log(String logMessage)
    {
        log.i(logMessage);
        Platform.runLater(() -> callback.addLogMessage(logMessage));
    }
}
