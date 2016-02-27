package com.aluto_benli.helloandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aluto_benli.helloandroid.model.HelloAndroidDBHelper;
import com.aluto_benli.helloandroid.model.History;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    protected static String LOG_TAG = MainActivity.class.getName();
    public final static String EXTRA_MESSAGE = "com.aluto_benli.helloandroid.MESSAGE";

    @NotEmpty(message = "Please type something.")
    private EditText fieldEditText;

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

        fieldEditText = (EditText) findViewById(R.id.edit_message);

        // Instantiate a new Validator
        final Validator validator = new Validator(this);
        validator.setValidationListener(this);

        // Validate on button onclick
        Button sendButton = (Button) findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        Context context = getBaseContext();
        String message  = "";
        File file;
        switch (storageType) {
            // Method 1: Reading from Shared Preferences and set the last value to EditText
            case "1":
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                message = sharedPref.getString(getString(R.string.user_input), "");
                Toast.makeText(context, "Read from SharedPreferences", Toast.LENGTH_SHORT).show();
                break;

            // Method 2: Reading data from the File saved on Internal Storage
            case "2":
                String fileName = context.getFilesDir() + File.separator + "data.txt";
                file = new File(fileName);
                if (file.exists()) {
                    message = readFileOnStorage(file);
                    Toast.makeText(context, message != null ? "Read from Internal Storage." : "Error while reading file", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "File does not exist.", Toast.LENGTH_SHORT).show();
                }
                break;

            // Method 3: Reading data from the File saved on External Storage
            case "3":
                file = this.getExternalStorage(context);
                if (file.exists()) {
                    if (this.isExternalStorageReadable()) {
                        message = readFileOnStorage(file);
                        Toast.makeText(context, message != null ? "Read from External Storage." : "Error while reading file", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "External storage not readable.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "File does not exist.", Toast.LENGTH_SHORT).show();
                }
                break;

            // Method 4: Reading data from SQLite database
            case "4":
                HelloAndroidDBHelper db = new HelloAndroidDBHelper(context);
                ArrayList<History> results = db.getHistories(15); // Get latest 15 history records only
                Integer displayCount = results.size();
                if (displayCount > 0) {
                    for (History history : results) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        message = message + dateFormat.format(new Date(history.getCreated())) + " ";
                        message = message + history.getMessage() + "\n";
                    }
                }

                // Show short alert message
                Integer total = db.getTotalHistories();
                String alertMessage = "Read from SQLite db";
                alertMessage = alertMessage + " showing " + displayCount.toString() + " of " + total.toString();
                Toast.makeText(context, alertMessage, Toast.LENGTH_SHORT).show();
                break;

            // No storage
            default:
                break;
        }

        TextView historyValue = (TextView) findViewById(R.id.input_history_value);
        TextView historyLabel = (TextView) findViewById(R.id.input_history_label);
        if (!message.isEmpty()) {
            // Display the last input value only when it was previously entered
            historyValue.setText(message);
            historyValue.setVisibility(TextView.VISIBLE);
            historyLabel.setVisibility(TextView.VISIBLE);
        } else {
            historyValue.setVisibility(TextView.INVISIBLE);
            historyLabel.setVisibility(TextView.INVISIBLE);
            if (!storageType.equals("0")) {
                Toast.makeText(context, "No message history", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * react to the user tapping/selecting an options menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Implement a ValidationListener
     * @param errors The list of ValidationError
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                // ((EditText) view).setError(message);
                // setError doesn't show error gracefully and
                // wordwrap to the next line in the EditText because of the error icon
                // so, just use Toast here
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Implement success ValidationListener
     */
    @Override
    public void onValidationSucceeded() {
        // sendMessage() call in XML layout file is moved here
        // because of onClickListener binding for the button in onCreate
        sendMessage();
    }

    /** Called when the user clicks the Send button */
    public void sendMessage() {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message = fieldEditText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
