package com.robotraccoons.debtnote.persistence.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.robotraccoons.debtnote.application.DebtNoteApplication;

import java.io.File;

public class DatabaseSingleton {

    private static final String DB = "DebtNote.db";

    private static SQLiteDatabase debtNoteDB = null;

    private DatabaseSingleton(){}

    public static SQLiteDatabase getInstance(){
        if(debtNoteDB == null){
            Context context = DebtNoteApplication.getAppContext();
            File dbpath = context.getDatabasePath(DB);
            if (!dbpath.getParentFile().exists()) {
                dbpath.getParentFile().mkdirs();
            }
            debtNoteDB = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
            UserSQL.onCreate(debtNoteDB);
            TransactionSQL.onCreate(debtNoteDB);
            PaymentSQL.onCreate(debtNoteDB);
        }
        return debtNoteDB;
    }
}
