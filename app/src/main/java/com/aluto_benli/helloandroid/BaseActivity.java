package com.aluto_benli.helloandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements Validator.ValidationListener {
    public final static String PACKAGE_NAME = "com.aluto_benli.helloandroid";
    protected final static String DATA_FILE_NAME = "data.txt";
    protected static String LOG_TAG = BaseActivity.class.getName();
    protected static String storageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reading storage type setting from Shared Preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultStorageType = getResources().getString(R.string.pref_default_storage_type);
        storageType = sharedPref.getString("prefStorageType", defaultStorageType);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public File getExternalStorage(Context context) {
        // Get the directory in the root directory for your app's private directory on the external storage.
        File dir = new File(getExternalFilesDir(null).toString());
        if (!dir.isDirectory() && !dir.mkdirs()) {
            Toast.makeText(context, "Directory not created", Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Directory not created");
        }

        return new File(dir, DATA_FILE_NAME);
    }

    /**
     * Display a simple alert dialog
     * @param message The message to show
     */
    public void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        alertDialog.show();
    }

    /**
     * Read and return the file content
     * @param file java.io.File
     * @return string or null
     */
    public String readFileOnStorage(File file) {
        String fileContent = "";
        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fileContent = sb.toString();
            streamReader.close();
            inputStream.close();
        } catch (IOException e) {
            showAlert(e.getMessage());
            Log.i(LOG_TAG, e.getMessage());
            return null;
        }
        return fileContent;
    }

    /**
     * Write a string to a file on storage
     * @param file java.io.File
     * @param content The string to write
     * @return boolean true on success; false on failure
     */
    public boolean writeFileOnStorage(File file, String content) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(stream);
            writer.println(content);
            writer.flush();
            writer.close();
            stream.close();
        } catch (IOException e) {
            showAlert(e.getMessage());
            Log.i(LOG_TAG, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Implement success ValidationListener
     */
    public void onValidationSucceeded() {}

    /**
     * Implement failure ValidationListener
     */
    public void onValidationFailed(List<ValidationError> errors) {}
}
