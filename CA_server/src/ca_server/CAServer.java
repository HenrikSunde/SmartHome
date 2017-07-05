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

/**
 * This Thread class is the server that connects
 * */
public class CAServer extends Thread
{
    private static final int PORT = 24576;
    private final String TAG = getClass().getSimpleName();
    private LogUtil log;
    private CAServerControllerCallback callback;
    private File keystoreFile = new File(Filepath.KEYSTORE);
    private String keystorePassword;
    private SSLServerSocket sslServer;

    /**
     * Constructor
     * */
    public CAServer(CAServerController callback, String keystorePassword)
    {
        log = new LogUtil(TAG);
        this.callback = callback;
        this.keystorePassword = keystorePassword;
    }

    /**
     * This SSL-server accepts client connections with TLS.
     * This method will run until the server (this thread) is interrupted.
     * */
    @Override
    public void run()
    {
        log("STARTED");

        try
        {
            FileInputStream keystoreStream = new FileInputStream(keystoreFile);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keystoreStream, keystorePassword.toCharArray());
            CloseableUtil.close(keystoreStream);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            sslServer = (SSLServerSocket) ssf.createServerSocket(PORT);
        }
        catch (Exception e)
        {
            log("Exception caught. Message: " + e.getMessage());
        }

        // Start a new thread for handling a client that connects
        try
        {
            log("Accepting new client connections...");
            while (!isInterrupted())
            {
                SSLSocket connection = (SSLSocket) sslServer.accept();
                log("Client connected " + connection.getInetAddress());
                new CAClientConnection(connection, callback, keystorePassword).start();
            }
        }
        catch (Exception e)
        {
            if (!e.getMessage().equals("socket closed"))
            {
                log("Exception caught. Message: " + e.getMessage());
            }
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
