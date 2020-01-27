package com.robotraccoons.debtnote.application;

import com.robotraccoons.debtnote.persistence.PaymentPersistence;
import com.robotraccoons.debtnote.persistence.SQLite.PaymentSQL;
import com.robotraccoons.debtnote.persistence.SQLite.TransactionSQL;
import com.robotraccoons.debtnote.persistence.SQLite.UserSQL;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;
import com.robotraccoons.debtnote.persistence.UserPersistence;
import com.robotraccoons.debtnote.persistence.stubs.UserPersistenceStub;
import com.robotraccoons.debtnote.persistence.stubs.PaymentPersistenceStub;
import com.robotraccoons.debtnote.persistence.stubs.TransactionPersistenceStub;



public class Services
{
    private static UserPersistence userPersistence = null;
    private static PaymentPersistence paymentPersistence = null;
    private static TransactionPersistence transactionPersistence = null;

    public static synchronized UserPersistence getUserPersistence()
    {
        if (userPersistence == null)
        {
            //userPersistence = new UserPersistenceStub();
            userPersistence = new UserSQL();
        }
        return userPersistence;
    }

    public static synchronized PaymentPersistence getPaymentPersistence() {
        if (paymentPersistence == null) {
           //paymentPersistence = new PaymentPersistenceStub();
           paymentPersistence = new PaymentSQL();
        }
        return paymentPersistence;
    }

    public static synchronized TransactionPersistence getTransactionPersistence() {
        if (transactionPersistence == null) {
            //transactionPersistence = new TransactionPersistenceStub();
            transactionPersistence = new TransactionSQL();
        }
        return transactionPersistence;
    }
}