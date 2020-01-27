package com.robotraccoons.debtnote.business;

import com.robotraccoons.debtnote.application.Services;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;

public class AccessTransactions {

    private TransactionPersistence dataAccess;
    private Transaction transaction;

    public AccessTransactions() {
        dataAccess = Services.getTransactionPersistence();
        transaction = null;}

    public AccessTransactions(TransactionPersistence transactionPersistence) {
        dataAccess = transactionPersistence;
        transaction = null;
    }

    public Transaction getTransactionById(final int id){
        transaction = dataAccess.getTransactionById(id);
        return dataAccess.getTransactionById(id);
    }

    public Transaction insertTransaction(final String newTransName, final String newTransDescription, final String payer){
        return dataAccess.insertTransaction(newTransName,newTransDescription, payer);
    }

    public void deleteTransactionById(final int id){
        transaction = dataAccess.getTransactionById(id);
        dataAccess.deleteTransactionById(id);
    }

    public int getNumTransactions() {
        return dataAccess.getNumTransactions();
    }
}
