package smarthome.smarthome_client.activity.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import smarthome.smarthome_client.R;
import smarthome.smarthome_client.activity.application.dinnerplanner.DinnerPlannerActivity;
import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

/**
 *
 * */
public class MainMenuActivity extends Activity
{
    private SharedPreferences sharedPrefs;
    private Intent shoppinglistIntent;
    private Intent dinnerPlannerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefs), MODE_PRIVATE);

        shoppinglistIntent = new Intent(this, ItemlistActivity.class);
        dinnerPlannerIntent = new Intent(this, DinnerPlannerActivity.class);
    }

    public void onMenuItemClicked(View view)
    {
        ImageView menuItem = (ImageView) view;

        switch (menuItem.getId())
        {
            case R.id.shoppinglist_imageview:
                startActivity(shoppinglistIntent);
                break;

            case R.id.dinner_planner_imageview:
                startActivity(dinnerPlannerIntent);
                break;

            default:
                break;
        }
    }
}
