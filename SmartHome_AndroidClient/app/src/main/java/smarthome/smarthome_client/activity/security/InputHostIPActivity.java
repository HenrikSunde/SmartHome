package smarthome.smarthome_client.activity.security;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import smarthome.smarthome_client.R;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class InputHostIPActivity extends Activity
{
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_host_ip);

        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), MODE_PRIVATE);
    }

    public void onOKButtonClick(View view)
    {
        EditText caServerIPAddress = (EditText) findViewById(R.id.caServerIPAddress_editText);
        String text = caServerIPAddress.getText().toString();

        if (text.length() < "0.0.0.0".length())
        {
            Toast.makeText(this, "Please write the IP address of the CA server.", Toast.LENGTH_LONG).show();
        }
        else
        {
            SharedPreferences.Editor spEdit = sharedPrefs.edit();
            spEdit.putString(getString(R.string.CAServerIPAddress), text);
            spEdit.apply();
            setResult(1);
            finish();
        }
    }
}
