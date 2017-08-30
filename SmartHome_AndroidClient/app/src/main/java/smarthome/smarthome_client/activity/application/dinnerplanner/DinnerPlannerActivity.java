package smarthome.smarthome_client.activity.application.dinnerplanner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import smarthome.smarthome_client.R;

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
