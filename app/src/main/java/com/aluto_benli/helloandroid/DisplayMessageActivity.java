package com.aluto_benli.helloandroid;

import android.content.Context;
//import android.content.SharedPreferences;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

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

        try {
            /*
            // Write to Shared Preferences
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.user_input), message);
            editor.commit();
            */

            /*
            // Save a File on Internal Storage
            Context context = getBaseContext();
            File file = new File(context.getFilesDir(), "data.txt");
            FileOutputStream stream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(stream);
            writer.println(message);
            writer.flush();
            writer.close();
            stream.close();

            // Show alerts
            Toast.makeText(context, "Your message is saved on internal storage.", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            */

            // Save a File on External Storage
            Context context = getBaseContext();
            File file = this.getExternalStorage(context);
            if (this.isExternalStorageWritable()) {
                FileOutputStream stream = new FileOutputStream(file);
                PrintWriter writer = new PrintWriter(stream);
                writer.println(message);
                writer.flush();
                writer.close();
                stream.close();

                // Show alerts
                Toast.makeText(context, "Your message is saved on external storage.", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "External storage not writable.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG, e.getMessage());
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
