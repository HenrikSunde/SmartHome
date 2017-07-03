package security;


import constant.Filepath;
import util.CloseableUtil;
import util.LogUtil;
import util.SocketWriterUtil;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.SecureRandom;

public class CAServerConnection extends Thread
{
    private String keystorePassword;
    LogUtil log = new LogUtil(getClass().getSimpleName());
    private SSLSocket connection;
    private DataInputStream connectionIn;
    private DataOutputStream connectionOut;
    private String host_ip;
    private final int PORT = 24576;

    public CAServerConnection(String keystorePassword, String host_ip)
    {
        this.keystorePassword = keystorePassword;
        this.host_ip = host_ip;
    }

    @Override
    public void run()
    {
        try
        {
            FileInputStream keystoreIn = new FileInputStream(Filepath.KEYSTORE);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keystoreIn, keystorePassword.toCharArray());
            CloseableUtil.close(keystoreIn);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            SSLSocketFactory sf = sslContext.getSocketFactory();
            connection = (SSLSocket) sf.createSocket(host_ip, PORT);

            connection.setUseClientMode(true);
            connection.setEnabledCipherSuites(connection.getSupportedCipherSuites());
            connection.startHandshake();

            connectionIn = new DataInputStream(connection.getInputStream());
            connectionOut = new DataOutputStream(connection.getOutputStream());

            SocketWriterUtil.writeString("TestClient1", connectionOut);
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
        }
        finally
        {
            CloseableUtil.close(connectionIn, connectionOut, connection);
        }
    }
}
