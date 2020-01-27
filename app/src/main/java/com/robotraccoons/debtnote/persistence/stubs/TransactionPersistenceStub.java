package com.robotraccoons.debtnote.persistence.stubs;

import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;

import java.util.ArrayList;
import java.util.List;

public class TransactionPersistenceStub implements TransactionPersistence {

    private List<Transaction> transactions;

    public TransactionPersistenceStub(){
        this.transactions = new ArrayList<>();
        this.transactions.add(new Transaction(transactions.size()+1,"Mobile bill","Mobile bill for May","marker"));
    }

    public Transaction getTransactionById(final int id) {
        Transaction returnVal = null;
        for (int i = 0; i < transactions.size(); i++)
        {
            if (transactions.get(i).getTransID() == id)
            {
                returnVal = transactions.get(i);
            }
        }
        return returnVal;
    }

    public Transaction insertTransaction(final String newTransName, final String newTransDescription, final String payer){
        Transaction t = new Transaction(transactions.size()+1,newTransName,newTransDescription, payer);
        transactions.add(t);
        return t;
    }

    public void deleteTransactionById(final int id){
        transactions.remove(getTransactionById(id));
    }

    public int getNumTransactions() {
        return transactions.size();
    }
}