package smarthome.smarthome_client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

        shoppinglistIntent = new Intent(this, ShoppinglistActivity.class);
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

            case R.id.bottom_imageview:
                Toast.makeText(this, "Bottom button pressed", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
