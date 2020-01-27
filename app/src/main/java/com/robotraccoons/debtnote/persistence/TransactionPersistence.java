package com.robotraccoons.debtnote.persistence;

import java.util.List;

import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;

public interface TransactionPersistence {
    Transaction getTransactionById(final int id);
    Transaction insertTransaction(final String newTransName, final String newTransDescription, final String payer);
    void deleteTransactionById(final int id);
    int getNumTransactions();
}
