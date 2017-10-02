package com.stan.androidlabs;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ListItemActivity";
    private static final int PHOTO_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        ImageButton photoButton = findViewById(R.id.photo_imageButton);

        Switch toastSwitch = findViewById(R.id.switch1);

        CheckBox dialogCheckBox = findViewById(R.id.checkBox);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(photoIntent, PHOTO_REQUEST_CODE);
                }
            }

        });

        toastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    CharSequence text = "Switch is On!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                } else {
                    CharSequence text = "Switch is Off!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });

        dialogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListItemActivity.this);
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title)
                            .setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("Response", "ListItemActivity passed: My information to share");
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageButton photoButton = findViewById(R.id.photo_imageButton);
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
