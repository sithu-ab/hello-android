package com.aluto_benli.helloandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.io.File;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements Validator.ValidationListener {
    public final static String PACKAGE_NAME = "com.aluto_benli.helloandroid";
    protected static String LOG_TAG = BaseActivity.class.getName();

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

        return new File(dir, "data.txt");
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
     * Implement success ValidationListener
     */
    public void onValidationSucceeded() {}

    /**
     * Implement failure ValidationListener
     */
    public void onValidationFailed(List<ValidationError> errors) {}
}
