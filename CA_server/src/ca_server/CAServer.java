package ca_server;

import callback.CAServerControllerCallback;
import constant.Filepath;
import controller.CAServerController;
import javafx.application.Platform;
import util.CloseableUtil;
import util.LogUtil;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.SecureRandom;

public class CAServer extends Thread
{
    private final String TAG = getClass().getSimpleName();
    private LogUtil log;
    private CAServerControllerCallback callback;
    private static final int PORT = 24576;
    private File keystoreFile = new File(Filepath.KEYSTORE);
    private String keystorePassword;
    private SSLServerSocket sslServer;
    
    public CAServer(CAServerController callback, String keystorePassword)
    {
        log = new LogUtil(TAG);
        this.callback = callback;
        this.keystorePassword = keystorePassword;
    }
    
    @Override
    public void run()
    {
        log("STARTED");
    
        try
        {
            FileInputStream keystoreStream = new FileInputStream(keystoreFile);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keystoreStream, keystorePassword.toCharArray());
    
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());
    
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            sslServer = (SSLServerSocket) ssf.createServerSocket(PORT);
    
            while (!isInterrupted())
            {
                log("Accepting new client connections...");
                SSLSocket connection = (SSLSocket) sslServer.accept();
                new CAClientConnection(connection, callback).start();
            }
        }
        catch (SocketException e) {}
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void interrupt()
    {
        super.interrupt();
        log("STOPPED");
        CloseableUtil.close(sslServer);
    }
    
    private void log(String logMessage)
    {
        log.i(logMessage);
        Platform.runLater(() -> callback.addLogMessage(logMessage));
    }
}
