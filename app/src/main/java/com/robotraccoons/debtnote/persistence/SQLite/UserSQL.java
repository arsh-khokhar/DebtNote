package com.robotraccoons.debtnote.persistence.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.UserPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserSQL implements UserPersistence {


    public static final String TABLE_NAME = "UserTB";

    public static final String USER_ID = "user_id";
    public static final String NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickname";

    public static final String LOG_TAG = "UserDB";

    private static final String CREATE_SQL_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    USER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " VARCHAR(100), " +
                    PASSWORD + " VARCHAR(50), " +
                    NICKNAME + " VARCHAR(100)" +
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

    public static User addValue(SQLiteDatabase db, int userId, String name, String password){
        ContentValues values = new ContentValues();
        values.put(USER_ID,userId);
        values.put(NAME,name);
        values.put(PASSWORD,password);
        db.insert(TABLE_NAME, null, values);
        User createdUser = new User(userId, name, password);
        return createdUser;
    }



    //security problem: getting all users information to your device
    public List<User> getUsers()
    {
        List<User> toReturn = new ArrayList<>();
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        String selectQuery = "SELECT *" +
                " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){         //read the user data from cursor
            do{
                String username = cursor.getString(cursor.getColumnIndex(NAME));
                String password = cursor.getString(cursor.getColumnIndex(PASSWORD));
                User user =new User(username, password); //user, trans, amtOwed,amtPaid );
                toReturn.add(user);
            }while(cursor.moveToNext());
        }
        toReturn.add(new User("Jaime", "12345678"));
        return Collections.unmodifiableList(toReturn);
      }


    public User getUserByUsername(String toFind) {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + UserSQL.NAME + ", " + UserSQL.PASSWORD + " FROM " + TABLE_NAME +
                " WHERE " + NAME + " = '" + toFind +"'";
        Cursor cur = db.rawQuery(selectQuery, null);
        cur.moveToFirst();
        int usernameIndex = cur.getColumnIndex(NAME);
        int passwordIndex = cur.getColumnIndex(PASSWORD);
        String username = cur.getString(usernameIndex);
        String password = cur.getString(passwordIndex);
        User returnUser = new User(username, password);
        return returnUser;
    }

    public int getUserId(String username) {
        int userId = -1;
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + USER_ID + " FROM " + TABLE_NAME +
                " WHERE " + NAME + " = '" + username+"'";
        Cursor cur = db.rawQuery(selectQuery, null);
        if(cur.moveToFirst()){
            userId = cur.getInt(cur.getColumnIndex(USER_ID));
        }

        return userId;
    }

    public User insertUser(final User currentUser)
    {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        User toReturn = addValue(db,currentUser.getUserID(), currentUser.getUserName(),currentUser.getUserPassword());
        return toReturn;
    }

    public void deleteUser(final User currentUser)
    {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        db.delete(TABLE_NAME, "user_name = ?", new String[]{currentUser.getUserName()+""});

    }

    public boolean validateUserLogin(final String username, final String password){
        boolean isValid = false;
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        Cursor userCusor = db.rawQuery("SELECT "+UserSQL.NAME+", "+UserSQL.PASSWORD+" " +
                "FROM userTB " +
                "WHERE "+ UserSQL.NAME+"='" + username + "' AND "+ UserSQL.PASSWORD +"='" + password + "'",null);
        if(userCusor.getCount() > 0){
            isValid = true;
        }
        return isValid;
    }

    public boolean hasUser(String username){
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + NAME + " FROM " + TABLE_NAME +
                " WHERE " + NAME + " = '" + username+"'";
        Cursor cur = db.rawQuery(selectQuery, null);
        if(cur.getCount() > 0){
            return true;
        }
        return false;
    }
}
