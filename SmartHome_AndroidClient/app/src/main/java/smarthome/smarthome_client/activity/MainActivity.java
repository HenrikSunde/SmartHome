package smarthome.smarthome_client.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.activity.application.MainMenuActivity;
import smarthome.smarthome_client.security.CACertificate_ServerConnection;
import smarthome.smarthome_client.security.CA_ServerConnection;

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

            CACertificate_ServerConnection caCertificateServerConnection = new CACertificate_ServerConnection(keystorePassword, host, context);
            caCertificateServerConnection.start();
            caCertificateServerConnection.join();

            CA_ServerConnection caServerConnection = new CA_ServerConnection(keystorePassword, host, context);
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
