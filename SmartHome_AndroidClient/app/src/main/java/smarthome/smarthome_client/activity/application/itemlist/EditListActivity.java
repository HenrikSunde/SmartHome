package smarthome.smarthome_client.activity.application.itemlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import smarthome.smarthome_client.R;

/***************************************************************************************************
 *
 **************************************************************************************************/
public class EditListActivity extends Activity
{
    private final String LOGTAG = getClass().getSimpleName();

    private EditText mItemList_Name_EditText;
    private ImageView mItemList_Icon_ImageView;
    private Switch mPublicSwitch;
    private Switch mSuggestionSwitch;
    private Button mCancelButton;
    private Button mSaveButton;
    private Button mDeleteButton;

    private String originalItemlistName = "";

    /**
     * ON CREATE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(LOGTAG, "*** Start of onCreate() ***");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_itemlist);

        mItemList_Name_EditText = (EditText) findViewById(R.id.itemlist_name_editText);
        mItemList_Icon_ImageView = (ImageView) findViewById(R.id.itemlist_icon_imageView);
        mPublicSwitch = (Switch) findViewById(R.id.public_switch);
        mSuggestionSwitch = (Switch) findViewById(R.id.suggestions_switch);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);

        setOnClickListeners();

        Intent intent = getIntent();
        if (intent.getBooleanExtra("edit", false))
        {
            mDeleteButton.setVisibility(View.VISIBLE);
            mDeleteButton.setClickable(true);

            originalItemlistName = intent.getStringExtra("itemlistName");
            mItemList_Name_EditText.setText(originalItemlistName);

            int iconID = intent.getIntExtra("icon", R.drawable.ic_crop_7_5_black_24dp);
            mItemList_Icon_ImageView.setBackground(getDrawable(iconID));

            boolean public_switchPosition = intent.getBooleanExtra("listPublic", false);
            mPublicSwitch.setChecked(public_switchPosition);

            boolean suggestions_switchPosition = intent.getBooleanExtra("suggestions", false);
            mPublicSwitch.setChecked(suggestions_switchPosition);
        }
        else
        {
            mDeleteButton.setVisibility(View.INVISIBLE);
            mDeleteButton.setClickable(false);
        }
    }

    private void setOnClickListeners()
    {
        mItemList_Icon_ImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), "Custom icon not implemented yet...", Toast.LENGTH_SHORT).show();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("deleted", originalItemlistName);
                setResult(ItemlistActivity.EDITLIST_DELETED, resultIntent);
                finish();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mItemList_Name_EditText.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please give the list a name", Toast.LENGTH_SHORT).show();
                    mItemList_Name_EditText.setText("");
                    return;
                }
                Intent resultIntent = new Intent();

                if (mItemList_Name_EditText.getText().toString().trim().equals(originalItemlistName))
                {
                    return;
                }
                else if (originalItemlistName.equals(""))
                {
                    // A new list is created
                    resultIntent.putExtra("newListTitle", mItemList_Name_EditText.getText().toString());
                    resultIntent.putExtra("icon", R.drawable.ic_crop_7_5_black_24dp); // TODO don't hardcode
                    resultIntent.putExtra("listPublic", mPublicSwitch.isChecked());
                    resultIntent.putExtra("suggestions", mSuggestionSwitch.isChecked());
                    setResult(ItemlistActivity.EDITLIST_NEWLIST, resultIntent);
                    finish();
                }
                else
                {
                    // The list is edited
                    resultIntent.putExtra("originalList", originalItemlistName);
                    resultIntent.putExtra("newListTitle", mItemList_Name_EditText.getText().toString());
                    resultIntent.putExtra("icon", R.drawable.ic_crop_7_5_black_24dp); // TODO don't hardcode
                    resultIntent.putExtra("listPublic", mPublicSwitch.isChecked());
                    resultIntent.putExtra("suggestions", mSuggestionSwitch.isChecked());
                    setResult(ItemlistActivity.EDITLIST_EDITED, resultIntent);
                    finish();
                }
            }
        });
    }
}
