package com.shore.game.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;

import com.shore.game.datalayer.dbhandlers.PlayersDbHandler;
import com.shore.game.utils.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * <p/>
 * A Singleton class for the database that manage all the transactions.
 * This class is the only entry for the Database component
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static SQLiteDatabase sDatabase;
    private static DatabaseManager sDatabaseManager;
    private final static String DATABASE_NAME = "cache";
    private final static int DATABASE_VERSION = 1; // Handled manually
    // Mark database connection should be closed or not
    private boolean mCloseConnecton = false;
    private final String TAG = "DatabaseManager";
    private Map<Integer, ArrayList<String>> mCreationQueriesMap = new HashMap<>();
    private ArrayList<String> mAlterQueries = new ArrayList<>();

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        openDatabase();
    }

    private void initializeCreationQueriesMap() {
        // DB version 1 tables creation queries
        ArrayList<String> dbVersion1Queries = new ArrayList<>();
        PlayersDbHandler playersDbHandler = new PlayersDbHandler();
        dbVersion1Queries.add(playersDbHandler.getCreationQuery());
        mCreationQueriesMap.put(DATABASE_VERSION, dbVersion1Queries);
    }

    public static synchronized DatabaseManager getInstance(Context context) throws Exception {
        if (sDatabaseManager == null) {
            // getDatabaseDirectory may throw exception if SD Card is unmounted
            sDatabaseManager = new DatabaseManager(context.getApplicationContext());
        }
        if (sDatabaseManager == null)
            throw new NullPointerException("Cannot Open Database Connection");
        return sDatabaseManager;
    }

    /**
     * A Method to open a connection with database
     *
     * @throws SQLException in case of fail to open connection with database
     */
    public void openDatabase() throws SQLException {
        try {
            sDatabase = this.getWritableDatabase();
            sDatabase.execSQL("PRAGMA foreign_keys=ON");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
                sDatabase.enableWriteAheadLogging();
        } catch (SQLiteFullException e) {
            sDatabase = this.getReadableDatabase();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
                sDatabase.enableWriteAheadLogging();
        } catch (SQLiteDiskIOException e) {
            // Handle Database not closed exception
            Log.e(TAG, "Database Connection Error", e);
            closeConnection();
        } catch (Exception e) {
            Log.e(TAG, "Database Connection Error", e);
        }
    }

    /**
     * Method in which all table queries should be executed on application first
     * run
     */
    private void createTables(SQLiteDatabase db) {
        try {
            ArrayList<String> creationQueries = null;
            for (Map.Entry<Integer, ArrayList<String>> entry : mCreationQueriesMap.entrySet()) {
                creationQueries = entry.getValue();
                if (creationQueries != null) {
                    for (int i = 0; i < creationQueries.size(); i++) {
                        db.execSQL(creationQueries.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            initializeCreationQueriesMap();
            createTables(db);
            alterTables(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            initializeCreationQueriesMap();
            updateTables(db, oldVersion, newVersion);
            alterTables(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            ArrayList<String> creationQueries = null;
            for (Map.Entry<Integer, ArrayList<String>> entry : mCreationQueriesMap.entrySet()) {
                if (entry.getKey() > oldVersion && entry.getKey() <= newVersion) {
                    creationQueries = entry.getValue();
                    if (creationQueries != null) {
                        for (int i = 0; i < creationQueries.size(); i++) {
                            db.execSQL(creationQueries.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.printError(e);
        }
    }

    private void alterTables(SQLiteDatabase db) {

        for (int i = 0; i < mAlterQueries.size(); i++) {
            try {
                db.execSQL(mAlterQueries.get(i));
            } catch (Exception e) {
                Log.printError(e);
            }
        }
    }

    /**
     * Checks if the database files exist in the application directory in the SD
     * card
     */
    private boolean checkdatabase(String path) {
        boolean checkdb = false;
        try {
            File dbFile = new File(path);
            checkdb = dbFile.exists();
        } catch (Exception e) {
            Log.printError(e);
        }
        return checkdb;
    }

    /*
     * Override of SqliteOpenHelper close method to close database connection
     */
    @Override
    public synchronized void close() {
        super.close();
        /*
         * Mark Database Manager singleton instance as null to insure opening db
		 * the next time getInstance called
		 */
        sDatabaseManager = null;
    }

    /**
     * Generic method to insert number of rows in any table into database (the
     * only way to store data into database)
     *
     * @param tableName :table will insert rows in
     * @param rows      list of rows (columns and values) to be inserted into table
     */
    public void insert(String tableName, ArrayList<ContentValues> rows) {
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen() && !sDatabase.isReadOnly()) {
                sDatabase.beginTransactionNonExclusive();
                for (int i = 0; i < rows.size(); i++) {
                    try {
                        sDatabase.insert(tableName, null, rows.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sDatabase.setTransactionSuccessful();

            } else {
                // TODO:: Handle returning status (success or fail)
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sDatabase.endTransaction();

            // Handle application closed and database connection marked as
            // should be closed
            if (mCloseConnecton) {
                closeConnection();
            }
        }
    }

    public long insert(String tableName, ContentValues row) {
        long result = -1;
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen() && !sDatabase.isReadOnly()) {
                try {
                    sDatabase.beginTransactionNonExclusive();
                    result = sDatabase.insert(tableName, null, row);
                    sDatabase.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, "Player row already exists");
                } finally {
                    sDatabase.endTransaction();
                }
            }
        } catch (Exception e) {
        } finally {
            // Handle application closed and database connection marked as
            // should be closed
            if (mCloseConnecton) {
                closeConnection();
            }
        }
        return result;
    }

    public void insertOrUpdate(String tableName, ArrayList<ContentValues> rows) {
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen() && !sDatabase.isReadOnly()) {
                sDatabase.beginTransactionNonExclusive();
                for (int i = 0; i < rows.size(); i++) {
                    try {
                        sDatabase.insertWithOnConflict(tableName, "", rows.get(i), SQLiteDatabase.CONFLICT_REPLACE);
                    } catch (Exception e) {
                        sDatabase.endTransaction();
                        e.printStackTrace();
                    }
                }
                sDatabase.setTransactionSuccessful();

            } else {
                // TODO:: Handle returning status (success or fail)
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sDatabase.endTransaction();
            // Handle application closed and database connection marked as
            // should be closed
            if (mCloseConnecton) {
                closeConnection();
            }
        }
    }

    public boolean insertOrUpdate(String tableName, ContentValues row) {
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen() && !sDatabase.isReadOnly()) {
                try {
                    sDatabase.beginTransactionNonExclusive();
                    sDatabase.insertWithOnConflict(tableName, "", row, SQLiteDatabase.CONFLICT_REPLACE);
                    sDatabase.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sDatabase.endTransaction();
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Handle application closed and database connection marked as
            // should be closed
            if (mCloseConnecton) {
                closeConnection();
            }
        }
        return true;
    }

    /**
     * This method delete all the data from a certain table.
     *
     * @param tableName The table name that you want to delete all the data from it.
     */
    public void deleteAllFromTable(String tableName) {
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen() && !sDatabase.isReadOnly()) {
                try {
                    sDatabase.delete(tableName, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // TODO:: Handle returning status (success or fail)
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Handle application closed and database connection marked as
            // should be closed
            if (mCloseConnecton) {
                closeConnection();
            }
        }
    }

    /**
     * Method to execute any selection query (the only way to retrieve data form
     * database)
     *
     * @param query :selection query to be executed
     * @return cursor
     */
    public Cursor select(String query) {
        Cursor cursor = null;
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen()) {
                cursor = sDatabase.rawQuery(query, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    /**
     * A method to execute a raw query
     *
     * @param query :query to be executed
     */
    public void executeQuery(String query) {
        try {
            {
                if (!sDatabase.isOpen())
                    openDatabase();
                if (sDatabase.isOpen()) {
                    sDatabase.execSQL(query);
                }
            }

        } catch (Exception e) {
            Log.printError(e);
        }

    }

    /**
     * A method to execute a raw query and returns cursor
     *
     * @param query :query to be executed
     */
    public Cursor rawQuery(String query, String[] selectedArgs) {
        Cursor cursor = null;
        try {
            if (!sDatabase.isOpen())
                openDatabase();
            if (sDatabase.isOpen()) {
                cursor = sDatabase.rawQuery(query, selectedArgs);
            }

        } catch (Exception e) {
            Log.printError(e);
        }
        return cursor;
    }


    /**
     * Call to close database connection (always called form UI)
     */
    public void closeConnection() {
        try {
            sDatabase.close();
            mCloseConnecton = false;
        } catch (Exception e) {
            mCloseConnecton = true;
        }
    }

}
