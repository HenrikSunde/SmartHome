package ca_server;

import constant.Filepath;
import util.*;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

/**
 * The CACertificateClientConnection is the thread that represent the connection to a client
 * that is requesting the root certificate.
 * When running, the root certificate will be streamed to the client. The connection will then close.
 * */
public class CACertificateClientConnection extends Thread
{
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    private Socket connection;
    private DataOutputStream connectionOut;
    private File rootCertFile = new File(Filepath.ROOT_CERT);
    
    /**
     * Constructor.
     * This class is only called from the server and the socket connection is passed in here.
     * */
    CACertificateClientConnection(Socket client)
    {
        this.connection = client;
    }
    
    /**
     * This method is called when the thread is started.
     * It will stream the root certificate through the socket's output stream.
     * */
    @Override
    public void run()
    {
        try
        {
            log.i("Sending certificate to client: " + connection.getInetAddress());
            connectionOut = new DataOutputStream(connection.getOutputStream());
            
            // Receive certificate with SocketReaderUtil.readString() on client side.
            SocketWriterUtil.writeString(FileReaderUtil.readString(rootCertFile), connectionOut);
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            // Close stream and socket connection
            CloseableUtil.close(connection);
        }
    }
}
