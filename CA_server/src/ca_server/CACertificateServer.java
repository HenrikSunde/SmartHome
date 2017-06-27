package ca_server;

import util.CloseableUtil;
import util.LogUtil;

import java.net.ServerSocket;
import java.net.Socket;

public class CACertificateServer extends Thread
{
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    private final int PORT = 24575;
    private ServerSocket serverSocket;
    
    @Override
    public void run()
    {
        try
        {
            log.i("STARTED");
            serverSocket = new ServerSocket(PORT);
    
            log.i("Accepting new client connections...");
            while (!isInterrupted())
            {
                Socket connection = serverSocket.accept();
                log.i("Client connected");
                new CACertificateClientConnection(connection).start();
            }
        }
        catch (Exception e)
        {
            log.i(e.getMessage());
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
