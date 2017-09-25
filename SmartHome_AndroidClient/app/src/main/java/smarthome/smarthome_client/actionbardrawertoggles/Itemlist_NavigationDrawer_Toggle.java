package smarthome.smarthome_client.actionbardrawertoggles;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import smarthome.smarthome_client.activity.application.itemlist.ItemlistActivity;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class Itemlist_NavigationDrawer_Toggle extends ActionBarDrawerToggle
{
    private ItemlistActivity mActivity;

    public Itemlist_NavigationDrawer_Toggle(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes)
    {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        mActivity = (ItemlistActivity) activity;
    }

    @Override
    public void onDrawerClosed(View drawerView)
    {
        super.onDrawerClosed(drawerView);
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerOpened(View drawerView)
    {
        super.onDrawerOpened(drawerView);
        mActivity.invalidateOptionsMenu();
        InputMethodManager imm = (InputMethodManager) drawerView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
    }
}
