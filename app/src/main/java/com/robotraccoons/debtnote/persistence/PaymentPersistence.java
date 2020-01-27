package com.robotraccoons.debtnote.persistence;

import java.util.List;

import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.Payment;

public interface PaymentPersistence {
    List<Payment> getPaymentsByUsername(final String username);
    List<Payment> getPaymentsByTransaction(final int transactionID);
    Payment insertPayment(String username, Transaction currentPayment, double amtOwed, double amtPaid);
    double getTotalAmountOwedByTransaction(final int id);
    double getRemainingAmountOwedByTransaction(final int id);
    void payToTransaction(final String username, final int tId, final double amount );
    void deleteTransactionPayments(final int id);
    double getUserOwedAmt(final int id, final String username);

}