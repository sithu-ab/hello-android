package com.aluto_benli.helloandroid.model;

/**
 * History Model that represents a History record
 */
public class History {
    private long mId;
    private String mMessage;
    private long mCreated;

    /**
     * Getter for History.id
     * @return The id
     */
    public long getId() {
        return mId;
    }

    /**
     * Setter for History.id
     * @param id The id of history to set
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * Getter for History.message
     * @return The message
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Setter for History.message
     * @param message The message of history to set
     */
    public void setMessage(String message) {
        mMessage = message;
    }

    /**
     * Getter for History.created
     * @return The created unix timestamp
     */
    public long getCreated() {
        return mCreated;
    }

    /**
     * Setter for History.created
     * @param created Unix timestamp
     */
    public void setCreated(long created) {
        mCreated = created;
    }
}
