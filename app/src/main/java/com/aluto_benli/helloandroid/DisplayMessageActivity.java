package com.aluto_benli.helloandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aluto_benli.helloandroid.model.HelloAndroidDBHelper;

import java.io.File;

public class DisplayMessageActivity extends BaseActivity {
    protected static String LOG_TAG = DisplayMessageActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the input message from Intent passed by the previous activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(textView);

        if (!storageType.equals("0")) {
            // If there is any storage type selected
            try {
                Context context = getBaseContext();
                File file;
                switch (storageType) {
                    // Method 1: Write to Shared Preferences
                    case "1":
                        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_input), message);
                        editor.commit();
                        Toast.makeText(context, "Saved in SharedPreferences.", Toast.LENGTH_SHORT).show();
                        break;

                    // Method 2: Save a File on Internal Storage
                    case "2":
                        file = new File(context.getFilesDir(), DATA_FILE_NAME);
                        if (writeFileOnStorage(file, message)) {
                            // Show alerts
                            Toast.makeText(context, "Saved on internal storage.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed to save on internal storage.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    // Method 3: Save a File on External Storage
                    case "3":
                        file = getExternalStorage(context);
                        if (isExternalStorageWritable()) {
                            if (writeFileOnStorage(file, message)) {
                                // Show alerts
                                Toast.makeText(context, "Saved on external storage.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Failed to save on external storage.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "External storage not writable.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "4":
                        // Method 4: Save data in SQL database
                        HelloAndroidDBHelper db = new HelloAndroidDBHelper(context);
                        long insertedId = db.insertHistory(message);
                        if (insertedId != -1) {
                            // Show alerts
                            Toast.makeText(context, "Saved in SQLite db.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to save in db.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    // No storage
                    default:
                        break;
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                showAlert(e.getMessage());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
