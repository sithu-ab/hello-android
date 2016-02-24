package com.aluto_benli.helloandroid;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class BaseActivity extends AppCompatActivity {
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
}