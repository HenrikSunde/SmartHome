package smarthome.smarthome_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.File;

import smarthome.smarthome_client.security.CACertificateServerConnection;
import smarthome.smarthome_client.security.CAServerConnection;

public class MainActivity extends Activity
{
    private Context context;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), MODE_PRIVATE);


        Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
        startActivity(mainMenuIntent);
        finish();

//        File keystoreFile = new File(context.getFilesDir(), getString(R.string.keystore_filename));
//        if (!keystoreFile.exists())
//        {
//            Intent hostIPIntent = new Intent(this, InputHostIPActivity.class);
//            startActivityForResult(hostIPIntent, 1);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {
            String host = sharedPrefs.getString(getString(R.string.CAServerIPAddress), "invalidIP");
            String keystorePassword = "123456";

            CACertificateServerConnection caCertificateServerConnection = new CACertificateServerConnection(keystorePassword, host, context);
            caCertificateServerConnection.start();
            caCertificateServerConnection.join();

            CAServerConnection caServerConnection = new CAServerConnection(keystorePassword, host, context);
            caServerConnection.start();
            caServerConnection.join();

            Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
            startActivity(mainMenuIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
