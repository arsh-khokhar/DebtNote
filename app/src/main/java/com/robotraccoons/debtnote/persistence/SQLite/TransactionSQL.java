package com.robotraccoons.debtnote.persistence.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionSQL implements TransactionPersistence {



    public static final String TABLE_NAME = "TransTB";

    public static final String TRANS_ID = "Trans_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PAYER = "payer";


    public static final String LOG_TAG = "TransactionSQL";

    private static final String CREATE_SQL_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    TRANS_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " VARCHAR(100), " +
                    PAYER + " VARCHAR(100), " +
                    DESCRIPTION + " VARCHAR(1000) " +
                    ");";


    public static void onCreate(SQLiteDatabase db){
        Log.i(LOG_TAG, CREATE_SQL_TABLE);
        db.execSQL(CREATE_SQL_TABLE);

    }

    public static void onUpgrade(SQLiteDatabase db, int old_VS, int new_VS){
        Log.i(LOG_TAG, "Upgrading database from version " + old_VS + " to " + new_VS + ". It will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public static Transaction addValue( SQLiteDatabase db, String name, String description, String payer){

        ContentValues values = new ContentValues();
        values.put(NAME,name);
        values.put(DESCRIPTION,description);
        values.put(PAYER,payer);
        db.insert(TABLE_NAME, null, values);
        String selectQuery = "SELECT MAX(" + TRANS_ID  + ") AS " + TRANS_ID + " FROM " + TABLE_NAME;
        Cursor cur = db.rawQuery(selectQuery, null);
        cur.moveToFirst();
        int transId = cur.getInt(cur.getColumnIndex(TRANS_ID));
        Transaction toReturn = new Transaction(transId, name, description, payer);
        return toReturn;
    }

    public static int delete(SQLiteDatabase db, String selection, String[] selectionArgs){
        int deleteCount = db.delete(TABLE_NAME, selection, selectionArgs);
        return deleteCount;
    }

    public List<Transaction> getAllTransactions()
    {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        List<Transaction> toReturn = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * " +
                                    " FROM "+ TABLE_NAME , null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int transID =  c.getInt(c.getColumnIndex(TRANS_ID));
                String name =  c.getString(c.getColumnIndex(NAME));
                String description = c.getString(c.getColumnIndex(DESCRIPTION));
                String payer = c.getString(c.getColumnIndex(PAYER));
                toReturn.add(new Transaction(transID, name, description, payer));
                c.moveToNext();
            }
        }

        return Collections.unmodifiableList(toReturn);
    }

    public Transaction getTransactionById(final int transId) {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        Transaction trans = null;
        String sqlQuery = "SELECT * " +
                          " FROM " + TABLE_NAME +
                          " WHERE " + TRANS_ID + " = " + transId;
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if(cursor.moveToFirst()) {
            String transName = cursor.getString(cursor.getColumnIndex(NAME));
            String transDesc = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
            String payer = cursor.getString(cursor.getColumnIndex(PAYER));
            trans = new Transaction(transId, transName, transDesc, payer);
        }
        return trans;
    }

    public Transaction insertTransaction(final String transactionName, final String transactionDescription, final String payer)
    {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        Transaction toReturn = addValue(db,transactionName,transactionDescription, payer);
        return toReturn;
    }

    public void deleteTransactionById(final int transID)
    {
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        delete(db, TRANS_ID+" = ?", new String[]{transID+""});
    }

    public int getNumTransactions() {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        String count = "COUNT";
        String selectQuery = "SELECT " + count + "(" + TRANS_ID +
                ") as " + count + " FROM " + TABLE_NAME;
        int numTransactions = 0;
        Cursor cur = db.rawQuery(selectQuery, null);
        if (cur.moveToFirst()) {
            numTransactions = cur.getInt(cur.getColumnIndex(count));
        }
        return numTransactions;
    }
}
