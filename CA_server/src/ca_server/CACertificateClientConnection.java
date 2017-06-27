package ca_server;

import constant.Filepath;
import util.CloseableUtil;
import util.FileToSocketStreamUtil;
import util.LogUtil;
import util.SocketWriterUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

public class CACertificateClientConnection extends Thread
{
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    
    private Socket connection;
    private DataOutputStream connectionOut;
    private File rootCertFile = new File(Filepath.ROOT_CERT);
    
    public CACertificateClientConnection(Socket client)
    {
        this.connection = client;
    }
    
    @Override
    public void run()
    {
        try
        {
            log.i("Sending certificate to client: " + connection.getInetAddress());
            connectionOut = new DataOutputStream(connection.getOutputStream());
            FileToSocketStreamUtil.doStream(rootCertFile, connectionOut);
        }
        catch (Exception e)
        {
            log.i(e.getMessage());
        }
        finally
        {
            CloseableUtil.close(connectionOut, connection);
        }
    }
}
