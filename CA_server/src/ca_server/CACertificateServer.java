package ca_server;

import util.CloseableUtil;
import util.LogUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
        catch (SocketException e)
        {
            log.i("Server stopped, non fatal SocketException caught. Message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
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
