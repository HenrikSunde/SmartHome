package security;


import constant.Filepath;
import constant.SysProp;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import util.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;

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


            // Generate RSA keypair, a self signed certificate and a CSR for the CA to sign
            log.i("Generating RSA keypair...");
            KeyPair keyPair = CryptographyGenerator.generateRSAKeyPair();

            log.i("Generating self-signed certificate...");
            X509Certificate selfSigned = CryptographyGenerator.generateSelfSignedCert("SmartHomeClient", 50, keyPair);

            log.i("Importing the self-signed certificate to the keystore...");
            keyStore.setCertificateEntry("self_signed", selfSigned);


            // Initiate the SSL context
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

            // Create a socket that is bound to the server
            log.i("Connecting to the CA to send a CSR and receive a signed certificate...");
            SSLSocketFactory sf = sslContext.getSocketFactory();
            connection = (SSLSocket) sf.createSocket(host_ip, PORT);
            connection.setUseClientMode(true);
            connection.setEnabledCipherSuites(connection.getSupportedCipherSuites());
            connection.startHandshake();

            // Get the socket input and output streams
            connectionIn = new DataInputStream(connection.getInputStream());
            connectionOut = new DataOutputStream(connection.getOutputStream());


            // Communicate with the server
            log.i("Sending my client ID to the server...");
            SocketWriterUtil.writeString("SmartHomeClient", connectionOut);

            log.i("Generating a csr...");
            JcaPKCS10CertificationRequest csr = CryptographyGenerator.generateCSR(keyPair, "SmartHomeClient");
            String csrString = CryptographyGenerator.pemObjectToString(csr);

            log.i("Sending csr to CA server...");
            SocketWriterUtil.writeString(csrString, connectionOut);

            log.i("Waiting for server to send back a signed certificate...");
            String signedCertString = SocketReaderUtil.readString(connectionIn);

            X509Certificate signedCert = (X509Certificate) CryptographyGenerator.stringToPemObject(signedCertString);
            X509Certificate rootCert = (X509Certificate) keyStore.getCertificate("SmartHomeCA");

            keyStore.setKeyEntry("SmartHomeClient", keyPair.getPrivate(), keystorePassword.toCharArray(), new Certificate[]{signedCert, rootCert});

            FileOutputStream keystoreOut = new FileOutputStream(Filepath.KEYSTORE);
            keyStore.store(keystoreOut, keystorePassword.toCharArray());
        }
        catch (Exception e)
        {
            log.i("Exception caught. Message: " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            CloseableUtil.close(connection);
        }
    }
}
