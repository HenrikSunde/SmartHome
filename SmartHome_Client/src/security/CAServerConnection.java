package security;

import constant.Filepath;
import util.CryptographyGenerator;
import util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;

public class CAServerConnection extends Thread
{
    private File keystoreFile = new File(Filepath.KEYSTORE);
    private LogUtil log = new LogUtil(getClass().getSimpleName());
    
    private String keystorePassword = "123456";
    
    @Override
    public void run()
    {
        if (keystoreFile.exists())
        {
            log.i("A KeyStore already exists");
        }
        else
        {
            log.i("A KeyStore does not exist, a new one will be created...");
            
            try
            {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(null, keystorePassword.toCharArray());
    
                KeyPair keyPair = CryptographyGenerator.generateRSAKeyPair();
                
    
                FileOutputStream keystoreOut = new FileOutputStream(keystoreFile);
                keyStore.store(keystoreOut, keystorePassword.toCharArray());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
