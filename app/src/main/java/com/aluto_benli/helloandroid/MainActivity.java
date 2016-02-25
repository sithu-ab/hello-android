package com.aluto_benli.helloandroid;

import android.content.Context;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aluto_benli.helloandroid.model.HelloAndroidDBHelper;
import com.aluto_benli.helloandroid.model.History;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    protected static String LOG_TAG = MainActivity.class.getName();
    public final static String EXTRA_MESSAGE = "com.aluto_benli.helloandroid.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
        // Method 1: Reading from Shared Preferences and set the last value to EditText
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String message = sharedPref.getString(getString(R.string.user_input), "");
        */

        /*
        // Method 2: Reading data from the File saved on Internal Storage
        Context context = getBaseContext();
        String fileName = context.getFilesDir() + File.separator + "data.txt";
        String message  = "";

        File file = new File(fileName);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                message = sb.toString();
                streamReader.close();
                inputStream.close();
            } catch (IOException e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        } else {
            Toast.makeText(context, "File does not exist.", Toast.LENGTH_SHORT).show();
        }
        */

        /*
        // Method 3: Reading data from the File saved on External Storage
        Context context = getBaseContext();
        String message  = "";

        File file = this.getExternalStorage(context);
        if (file.exists()) {
            if (this.isExternalStorageReadable()) {
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    InputStreamReader streamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    message = sb.toString();
                    streamReader.close();
                    inputStream.close();
                } catch (IOException e) {
                    Log.i(LOG_TAG, e.getMessage());
                }

            } else {
                Toast.makeText(context, "External storage not readable.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "File does not exist.", Toast.LENGTH_SHORT).show();
        }
        */

        // Method 4: Reading data from SQLite database
        Context context = getBaseContext();
        String message  = "";
        HelloAndroidDBHelper db = new HelloAndroidDBHelper(context);
        ArrayList<History> results = db.getHistories(10); // Get latest 10 history records only
        Integer displayCount = results.size();
        if (displayCount > 0) {
            for (History history : results) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                message = message + dateFormat.format(new Date(history.getCreated())) + " ";
                message = message + history.getMessage() + "\n";
            }
        }

        if (!message.isEmpty()) {
            // Display the last input value only when it was previously entered
            TextView historyValue = (TextView) findViewById(R.id.input_history_value);
            historyValue.setText(message);
            historyValue.setVisibility(TextView.VISIBLE);

            TextView historyLabel = (TextView) findViewById(R.id.input_history_label);
            historyLabel.setVisibility(TextView.VISIBLE);

            // Show short alert message
            Toast.makeText(context, "Read from SQLite db", Toast.LENGTH_SHORT).show();

            Integer total = db.getTotalHistories();
            Toast.makeText(context, "Showing " + displayCount.toString() + " out of " + total.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
