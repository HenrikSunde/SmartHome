package smarthome.smarthome_client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class DinnerPlannerActivity extends Activity
{
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_planner);

        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), MODE_PRIVATE);
    }
}
