import ca_server.CACertificateServer;
import constant.Filepath;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.Security;

public class Run_CA_Server extends Application
{
    public static void main(String[] args)
    {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        
        // Create necessary directories
        new File(Filepath.LOG_DIR).mkdirs();
        new File(Filepath.SECURITY_DIR).mkdirs();
        new File(Filepath.TEMP_CERTS_DIR).mkdirs();
        
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/CA_server.fxml"));
    
        primaryStage.setResizable(false);
        primaryStage.setTitle("CA server");
        primaryStage.setScene(new Scene(root, 970, 600));
        primaryStage.show();
    }
}
