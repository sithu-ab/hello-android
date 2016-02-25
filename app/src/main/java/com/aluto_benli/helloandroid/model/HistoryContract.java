package com.aluto_benli.helloandroid.model;

import android.provider.BaseColumns;

/**
 * This class represents a contract for a history table
 * to store user inputs
 */
public final class HistoryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public HistoryContract() {}

    /**
     * Contains the name of the table to store user inputs
     */
    public static final String TABLE_NAME = "history";

    /**
     * Contains the SQL query to use to create the table.
     */
    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + HistoryContract.TABLE_NAME + " ("
            + HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HistoryEntry.COLUMN_NAME_MESSAGE + " TEXT NOT NULL, "
            + HistoryEntry.COLUMN_NAME_CREATED + " INTEGER NOT NULL "
            + ");";

    /**
     * Contains the SQL query to use to drop the table.
     */
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + HistoryContract.TABLE_NAME;

    /**
     * This inner class defines the columns in the history table.
     * The primary key is the _ID column from the BaseColumn class that
     * could be accessible through HistoryContract._ID
     */
    public static abstract class HistoryEntry implements BaseColumns {
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_CREATED = "created";
    }
}
