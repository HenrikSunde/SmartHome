package ca_server;

import util.CloseableUtil;
import util.LogUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the server that will listen for clients requesting the root certificate.
 */
public class CACertificateServer extends Thread
{
    private final int PORT = 24575;
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    private ServerSocket serverSocket;

    /**
     * Will listen for new clients as long as the thread is uninterrupted.
     * Clients connected will have their connection handled in a new thread.
     */
    @Override
    public void run()
    {
        try
        {
            log.i("STARTED");
            serverSocket = new ServerSocket(PORT);
        }
        catch (IOException e)
        {
            log.i("IOException caught. Message: " + e.getMessage());
        }

        try
        {
            log.i("Accepting new client connections...");
            while (!isInterrupted())
            {
                Socket connection = serverSocket.accept();
                log.i("Client connected");
                new CACertificateClientConnection(connection).start();
            }
        }
        catch (IOException e)
        {
            log.i("IOException caught. Message: " + e.getMessage());
        }
    }

    @Override
    public void interrupt()
    {
        super.interrupt();
        log.i("STOPPED");
        CloseableUtil.close(serverSocket);
    }
}
