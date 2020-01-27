package com.robotraccoons.debtnote.persistence.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.PaymentPersistence;

import java.util.ArrayList;
import java.util.List;

public class PaymentSQL implements PaymentPersistence {
    private static final String TABLE_NAME = "PaymentTB";

    private static final String PAY_ID = "payment_id";
    //private static final String NAME = "name";
    //private static final String COMMENT = "comment";
    private static final String TRANS_ID = "transaction_id";
    private static final String USER_NAME = "user_name";
    private static final String AMT_OWED = "amt_owed";
    private static final String AMT_PAID = "amt_paid";
    private static final String CREATED_DATE = "created_date";
    private AccessUsers users = new AccessUsers();
    private AccessTransactions transactions = new AccessTransactions();

    private static final String LOG_TAG = "PaymentDB";
    private static final String CREATE_SQL_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    PAY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    //NAME + " VARCHAR(1000), " +
                    //COMMENT + " VARCHAR(1000), " +
                    USER_NAME + " VARCHAR(100) /*NOT NULL*/," +
                    TRANS_ID + " INTEGER /*NOT NULL*/," +
                    AMT_OWED + " NUMERIC(18,2), " + 
                    AMT_PAID + " NUMERIC(18,2), " +
                    CREATED_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP " +
                    // "FOREIGN KEY(" + GROUP_ID + ") REFERENCES "+ GroupDB.TABLE_NAME +"("+ GROUP_ID +")"+
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

    public PaymentSQL(){

    }

    @Override
    public List<Payment> getPaymentsByUsername(String username){
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        List<Payment> paymentList = new ArrayList<Payment>();
        User user = users.getUserByUsername(username);
        String selectQuery = "SELECT *" +
                             " FROM " + TABLE_NAME +
                             " WHERE " + USER_NAME + " = '" + username+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst()){         //read the user data from cursor
            do{
                int paymentID = cursor.getInt(cursor.getColumnIndex(PAY_ID));
                int transID = cursor.getInt(cursor.getColumnIndex(TRANS_ID));
                double amtOwed = cursor.getDouble(cursor.getColumnIndex(AMT_OWED));
                double amtPaid = cursor.getDouble(cursor.getColumnIndex(AMT_PAID));

                Transaction trans = transactions.getTransactionById(transID);

                Payment currPayment =new Payment(paymentID, user, trans, amtOwed,amtPaid );
                paymentList.add(currPayment);
                System.out.println("Adding payment " + currPayment.getTransName() + " " + currPayment.getTransDescription() + currPayment.getUser().getUserName());

            }while(cursor.moveToNext());
        }

        return paymentList;
    }

    @Override
    public List<Payment> getPaymentsByTransaction(int transID) {
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        List<Payment> paymentList = new ArrayList<Payment>();
        String selectQuery = "SELECT *" +
                             " FROM " + TABLE_NAME +
                             " WHERE " + TRANS_ID + " = " + transID;
        Cursor cur = db.rawQuery(selectQuery, null);
        //int paymentColIndex = cur.getColumnIndex(PAY_ID);
        int userColIndex = cur.getColumnIndex(USER_NAME);
        int amtOwedIndex = cur.getColumnIndex(AMT_OWED);
        int amtPaidIndex = cur.getColumnIndex(AMT_PAID);
        while (cur.moveToNext())
        {
            //int paymentId = cur.getInt(paymentColIndex);
            String username = cur.getString(userColIndex);
            double amt_owed = cur.getDouble(amtOwedIndex);
            double amt_paid = cur.getDouble(amtPaidIndex);
            Transaction currTransaction = transactions.getTransactionById(transID);
            User currUser = users.getUserByUsername(username);
            Payment currPayment = new Payment(currUser, currTransaction, amt_owed, amt_paid);
            paymentList.add(currPayment);
            System.out.println("Adding payment " + currPayment.getTransName() + " " + currPayment.getTransDescription() + currPayment.getUser().getUserName());
        }
        return paymentList;
    }

    @Override
    public Payment insertPayment(String username, Transaction currentPayment, double amtOwed, double amtPaid) {
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        Payment payment = null;
        ContentValues values = new ContentValues();
        AccessUsers accessUsers = new AccessUsers();
        //int userID = accessUsers.getUserId(username);
        User user = accessUsers.getUserByUsername(username);

        payment = new Payment(user, currentPayment, amtOwed, amtPaid);

        values.put(USER_NAME, username);
        values.put(AMT_OWED, amtOwed);
        values.put(AMT_PAID, amtPaid);
        values.put(TRANS_ID, currentPayment.getTransID());

        db.insert(TABLE_NAME, null, values);
        Cursor paymentCursor = db.rawQuery("SELECT * "+
                        "FROM paymentTB "+
                        "WHERE "+ PaymentSQL.USER_NAME+"='"+username+"'",
                null);
        paymentCursor.moveToFirst();
        System.out.println("AAAAAA"+paymentCursor.getInt(paymentCursor.getColumnIndex(PaymentSQL.PAY_ID)));
        return payment;
    }

    @Override
    public double getRemainingAmountOwedByTransaction(final int transId){
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + TRANS_ID + ", " + AMT_OWED +
                ", " + AMT_PAID + " FROM " + TABLE_NAME + " WHERE " + TRANS_ID + " = " + transId;
        Cursor cur = db.rawQuery(selectQuery, null);
        int amtOwedIndex = cur.getColumnIndex(AMT_OWED);
        int amtPaidIndex = cur.getColumnIndex(AMT_PAID);
        double amt_owed = 0.0;
        double amt_paid = 0.0;
        while (cur.moveToNext())
        {
            amt_owed += cur.getDouble(amtOwedIndex);
            amt_paid += cur.getDouble(amtPaidIndex);
        }
        System.out.println("Adding amount owed/paid for : " + transId + ": " + amt_owed + "," + amt_paid);
        return amt_owed - amt_paid;
    }

    @Override
    public double getTotalAmountOwedByTransaction(final int transId){
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + TRANS_ID + ", " + AMT_OWED +
                " FROM " + TABLE_NAME + " WHERE " + TRANS_ID + " = " + transId;
        Cursor cur = db.rawQuery(selectQuery, null);
        int amtOwedIndex = cur.getColumnIndex(AMT_OWED);
        int amtPaidIndex = cur.getColumnIndex(AMT_PAID);
        double amt_owed = 0.0;
        while (cur.moveToNext())
        {
            amt_owed += cur.getDouble(amtOwedIndex);
        }
        System.out.println("Adding amount owed/paid for : " + transId + ": " + amt_owed);
        return amt_owed;
    }

    @Override
    public void deleteTransactionPayments(final int transID){
        SQLiteDatabase db= DatabaseSingleton.getInstance();

        int numDeleted = db.delete(TABLE_NAME, TRANS_ID + "=" + transID, null);
        System.out.println(numDeleted + " rows deleted for transaction id " + transID);
    }

    @Override
    public void payToTransaction(String username, int id, double amount){
        //Log.d("AMOUNT TO BE PAID", ""+amount);
        SQLiteDatabase db= DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + AMT_PAID + " FROM " + TABLE_NAME + " WHERE " + TRANS_ID  + " = " + id +" AND " + USER_NAME + "='" + username+"'";
        Cursor cur = db.rawQuery(selectQuery, null);
        cur.moveToFirst();

        Log.d("HEY",""+cur.getDouble(cur.getColumnIndex(AMT_PAID)));
        double oldVal = cur.getDouble(cur.getColumnIndex(AMT_PAID));
        //Log.d("AMOUNT CURRENTLY", ""+p);
        ContentValues values = new ContentValues();
        values.put(AMT_PAID, oldVal + amount);
        db.update(TABLE_NAME, values, USER_NAME + "='" + username+ "'" + " AND " + TRANS_ID +"=" +id, null); //, new String[]{username, id+""});
        Log.d("ROWS AFFECTED",""+db.update(TABLE_NAME, values, USER_NAME + "=? AND " + TRANS_ID +"=?", new String[]{username, id+""}));
    }

    @Override
    public double getUserOwedAmt(final int id, final String username) {
        SQLiteDatabase db = DatabaseSingleton.getInstance();
        String selectQuery = "SELECT " + AMT_OWED + ", " + AMT_PAID +
                " FROM " + TABLE_NAME + " WHERE " + TRANS_ID  + " = " + id +" AND " + USER_NAME + "='" + username+"'";
        double amountToReturn = 0.0;
        Cursor cur = db.rawQuery(selectQuery, null);
        cur.moveToFirst();
        amountToReturn = cur.getDouble(cur.getColumnIndex(AMT_OWED)) - cur.getDouble(cur.getColumnIndex(AMT_PAID));
        Log.d("TO RETURN", amountToReturn+"");
        amountToReturn = (double)Math.round(amountToReturn * 100d) / 100d;
        return amountToReturn;
    }
}
