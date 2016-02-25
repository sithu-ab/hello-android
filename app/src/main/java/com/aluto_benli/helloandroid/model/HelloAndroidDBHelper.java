package com.aluto_benli.helloandroid.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * This class helps open, create, and upgrade the database file
 */
public class HelloAndroidDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    // The name of the database file on the file system
    public static final String DATABASE_NAME = "HelloAndroid.db";
    // The log tag
    public static final String LOG_TAG = HelloAndroidDBHelper.class.getName();

    public HelloAndroidDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the underlying database with the SQL_CREATE_TABLE queries from
     * the contract classes to create the tables and initialize the data.
     * The onCreate is triggered the first time someone tries to access
     * the database with the getReadableDatabase or
     * getWritableDatabase methods.
     *
     * @param db the database being accessed and that should be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database to contain the data for the projects
        db.execSQL(HistoryContract.SQL_CREATE_TABLE);
    }

    /**
     * This method must be implemented if your application is upgraded and must
     * include the SQL query to upgrade the database from your old to your new
     * schema.
     *
     * @param db the database being upgraded.
     * @param oldVersion the current version of the database before the upgrade.
     * @param newVersion the version of the database after the upgrade.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Log that the database is being upgraded
        Log.i(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // This database is only a cache for history data, so its upgrade policy is
        // to simply to discard the data and start over
        migrate(db);
    }

    /**
     * This method may be implemented if your application is downgraded and must
     * include the SQL query to downgrade the database from your old to your new
     * schema.
     *
     * @param db the database being downgraded.
     * @param oldVersion the current version of the database before the downgrade.
     * @param newVersion the version of the database after the downgrade.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Log that the database is being downgraded
        Log.i(LOG_TAG, "Downgrading database from version " + oldVersion + " to " + newVersion);
        // This database is only a cache for history data, so its downgrade policy is
        // to simply to discard the data and start over
        migrate(db);
    }

    /**
     * Called from onUpgrade and onDowngrade
     * @param db the database being upgraded or downgraded
     */
    private void migrate(SQLiteDatabase db) {
        db.execSQL(HistoryContract.SQL_DELETE_TABLE);
        onCreate(db);
    }

    /**
     * Insert a new record to the history table
     * @param entry The history input value
     * @return
     */
    public long insertHistory(String entry) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_MESSAGE, entry);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_CREATED, System.currentTimeMillis());

        // Insert the new row, returning the primary key value of the new row
        long historyId;
        historyId = db.insert(HistoryContract.TABLE_NAME, null, values);

        return historyId;
    }

    /**
     * Read all records from the history table
     * @return
     */
    public ArrayList<History> getHistories(Integer...limit){
        // Gets the database in the current database helper in read-only mode
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<History> histories = new ArrayList<History>();

        // Define the columns from the database
        // you will actually use after this query.
        String[] columns = {
                HistoryContract.HistoryEntry._ID,
                HistoryContract.HistoryEntry.COLUMN_NAME_MESSAGE,
                HistoryContract.HistoryEntry.COLUMN_NAME_CREATED
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = HistoryContract.HistoryEntry.COLUMN_NAME_CREATED + " DESC";
        // LIMIT clause for query (optional parameter)
        String maxResults = limit.length > 0 ? limit[0].toString() : null;

        // After the query, the cursor points to the first database row
        // returned by the request
        Cursor c = db.query(
                HistoryContract.TABLE_NAME, // table name
                columns, // columns to be fetched; null for all columns
                null, // where condition
                null, // argument values for where condition
                null, // group by
                null, // having
                sortOrder, // order by
                maxResults // limit
        );
        while (c.moveToNext()) {
            History history = new History();
            history.setId(c.getLong(c.getColumnIndex(HistoryContract.HistoryEntry._ID)));
            history.setMessage(c.getString(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_MESSAGE)));
            history.setCreated(c.getLong(c.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_CREATED)));
            histories.add(history);
        }
        c.close();
        return histories;
    }
}
