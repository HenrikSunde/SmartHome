package controller;

import ca_server.CACertificateServer;
import ca_server.CAServer;
import ca_server.ClientConnection;
import callback.CAServerControllerCallback;
import constant.Filepath;
import constant.SysProp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import util.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class CAServerController implements Initializable, CAServerControllerCallback
{
    @FXML
    private Button start_server_button;
    
    @FXML
    private Button stop_server_button;
    
    @FXML
    private ScrollPane pending_requests_scrollPane;
    
    @FXML
    private VBox pending_requests;
    
    @FXML
    private Label version;
    
    @FXML
    private ScrollPane log_messages_scrollPane;
    
    @FXML
    private VBox log_messages;
    
    @FXML
    private VBox certificate_signing_request;
    
    @FXML
    private Label client_identity;
    
    @FXML
    private Button sign_certifficate_button;
    
    @FXML
    private Button delete_request_button;
    
    private CAServer caServer;
    private CACertificateServer caCertificateServer;
    private File keystoreFile = new File(Filepath.KEYSTORE);
    private File rootCertFile = new File(Filepath.ROOT_CERT);
    private LogUtil log;
    
    private String keystorePassword = "123456";
    private int keySize = 2048;
    private String CN = "SmartHome";
    private int validYears = 50;
    private String alias = "SmartHomeCA";
    
    @FXML
    void onStartServerButtonClick(ActionEvent event)
    {
        start_server_button.setDisable(true);
        stop_server_button.setDisable(false);
        
        if (setUpKeystore())
        {
            caCertificateServer = new CACertificateServer();
            caCertificateServer.start();
            
            caServer = new CAServer(this, keystorePassword);
            caServer.start();
        }

//        certificate_signing_request.setOpacity(1);
    }
    
    @FXML
    void onStopServerButtonClick(ActionEvent event)
    {
        stop_server_button.setDisable(true);
        start_server_button.setDisable(false);
        
        caCertificateServer.interrupt();
        caServer.interrupt();

//        certificate_signing_request.setOpacity(0);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        log = new LogUtil(getClass().getSimpleName());
        
        Label logStarted_label = new Label("Logging started...");
        log_messages.getChildren().add(logStarted_label);
        
        log_messages_scrollPane.vvalueProperty().bind(log_messages.heightProperty());
        pending_requests_scrollPane.vvalueProperty().bind(pending_requests.heightProperty());
    }
    
    @Override
    public void addLogMessage(String logMessage)
    {
        log_messages.getChildren().add(new Label(logMessage));
    }
    
    private void log(String logMessage)
    {
        log.i(logMessage);
        addLogMessage(logMessage);
    }
    
    @Override
    public void clientConnected(String clientID, String connectTime, CountDownLatch latch)
    {
        Label clientID_Label = new Label(clientID);
        clientID_Label.setFont(new Font(12));
        
        Label connectTime_Label = new Label(connectTime);
        connectTime_Label.setFont(new Font(12));
        
        VBox client_VBox = new VBox(clientID_Label, connectTime_Label);
        client_VBox.setMinWidth(246);
        client_VBox.setMaxWidth(246);
        client_VBox.setOnMouseClicked((e) ->
        {
            certificate_signing_request.setOpacity(1);
            latch.countDown();
        });
        
        pending_requests.getChildren().add(client_VBox);
    }
    
    private boolean setUpKeystore()
    {
        if (keystoreFile.exists() && rootCertFile.exists())
        {
            log("A KeyStore and a self-signed certificate already exists");
        }
        else
        {
            FileUtil.delete(keystoreFile, rootCertFile);
            
            log("Creating new KeyStore...");
            
            try (FileOutputStream fileOut = new FileOutputStream(Filepath.KEYSTORE))
            {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                log("Loading a new Java KeyStore...");
                keyStore.load(null, keystorePassword.toCharArray());
                
                //Cryptography
                //------------------------------------------------------------------------------------------------------
                log("Generating RSA keypair...");
                KeyPair keyPair = CryptographyGenerator.generateRSAKeyPair(keySize);
                
                log("Generating self-signed certificate...");
                X509Certificate selfSignedCert = CryptographyGenerator.generateSelfSignedCert(CN, validYears, keyPair);
                
                log("Storing the self-signed certificate in the KeyStore...");
                keyStore.setCertificateEntry(alias, selfSignedCert);
                
                log("Exporting the self-signed certificate to a file...");
                String rootCertString = CryptographyGenerator.certificateToString(selfSignedCert);
                FileWriterUtil.writeString(rootCertString, false, rootCertFile);
                //------------------------------------------------------------------------------------------------------
                
                log("Storing the Keystore in a file...");
                keyStore.store(fileOut, keystorePassword.toCharArray());
            }
            catch (Exception e)
            {
                log("Exception caught. Undoing all changes... " + e.getMessage());
                FileUtil.delete(keystoreFile, rootCertFile);
                return false;
            }
        }
        return true;
    }
}
