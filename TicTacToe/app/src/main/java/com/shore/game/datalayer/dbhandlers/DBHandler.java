package com.shore.game.datalayer.dbhandlers;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * @param <T> : A template class which will be used in overrides
 */
public abstract class DBHandler<T> {

    public final String TAG = "DBHandler";

    /**
     * A generic method to be overridden for generating table creation query
     *
     * @return create query
     */
    public abstract String getCreationQuery();

    /**
     * A generic method to be overridden for altering table
     *
     * @return A string contain the alter query
     */
    public abstract ArrayList<String> getAlterQueries();

    /**
     * A generic method to be overridden for generating table selection query
     *
     * @return create query
     */
    protected abstract String getSelectQuery();

    /**
     * A generic method to be overridden for generating rows deletion query
     *
     * @return create query
     */
    protected abstract String getDeleteQuery();

    /**
     * A generic method to be overridden to parse Dataset from cursor
     *
     * @param cursor :A pointer on Dataset
     */
    protected ArrayList<T> parseDataSet(Cursor cursor) {
        return new ArrayList<>();
    }


    /**
     * A generic method to be overridden to parse one row from Dataset cursor
     *
     * @param cursor :A pointer on Dataset
     */
    protected T parseDataset(Cursor cursor) {
        return null;
    }

    /**
     * A generic method to be overridden for storing a list of any type in
     * database
     *
     * @param list : collection to be saved on database
     * @return success boolean
     */
    public boolean saveAll(Context context, ArrayList<T> list) {
        return false;
    }


    /**
     * A generic method to be overridden for retrieving list from database
     *
     * @param context
     * @return A list of type T which retrieved from database
     */
    @Nullable
    public ArrayList<T> getAll(Context context) {
        return null;
    }

    /**
     * A generic method to be overridden for retrieving one row from database
     *
     * @param context
     * @return One item of type T which retrieved from database
     */
    @Nullable
    public T getItem(Context context) {
        return null;
    }

    /**
     * A generic method to be overridden for storing a list of any type in
     * database
     *
     * @param list : collection to be saved on database
     * @param page : indicates the page number
     * @return success boolean
     */
    public boolean saveAll(Context context, ArrayList<T> list, int page) {
        return false;
    }

    /**
     * A generic method to be overridden for storing an item of any type in
     * database
     *
     * @param item : item to be saved on database
     * @return success boolean
     */
    public boolean saveItem(Context context, T item) {
        return false;
    }

}
