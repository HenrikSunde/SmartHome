package smarthome.smarthome_client;

import android.app.Activity;
import android.os.Bundle;

import smarthome.smarthome_client.security.CACertificateServerConnection;
import smarthome.smarthome_client.security.CAServerConnection;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            CACertificateServerConnection caCertificateServerConnection = new CACertificateServerConnection("123456", "192.168.1.105", getApplicationContext());
            caCertificateServerConnection.start();
            caCertificateServerConnection.join();

            CAServerConnection caServerConnection = new CAServerConnection("123456", "192.168.1.105", getApplicationContext());
            caServerConnection.start();
            caServerConnection.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
