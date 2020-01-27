package com.robotraccoons.debtnote.business;

import com.robotraccoons.debtnote.application.Services;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.persistence.PaymentPersistence;

import java.util.List;

public class AccessPayments
{
    private PaymentPersistence dataAccess;
    private List<Payment> payments;

    public AccessPayments(){
        dataAccess = Services.getPaymentPersistence();
        payments = null;  }

    public AccessPayments(PaymentPersistence paymentPersistence){
        dataAccess = paymentPersistence;
        payments = null;
    }

    public List<Payment> getAllPayments(String username){
        payments = dataAccess.getPaymentsByUsername(username);
        return payments;
    }

    public Payment insertPayment(String username, Transaction currentPayment, double amtOwed, double amtPaid){
        Payment payment = dataAccess.insertPayment(username,currentPayment,amtOwed,amtPaid);
        return payment;
    }

    public double getTotalAmountOwedByTransaction(final int id){
        double amountOwed = dataAccess.getTotalAmountOwedByTransaction(id);
        return amountOwed;
    }

    public double getRemainingAmountOwedByTransaction(final int id)
    {
        double amountOwed = dataAccess.getRemainingAmountOwedByTransaction(id);
        return amountOwed;
    }

    public void payToTransaction(final String username, final int tId, final double amount )
    {
        dataAccess.payToTransaction(username,tId,amount);
    }

    public void deleteTransactionPayments(final int id)
    {
        dataAccess.deleteTransactionPayments(id);
    }

    public double getUserOwedAmt(final int id, final String username)
    {
        double owedAmount = dataAccess.getUserOwedAmt(id,username);
        return owedAmount;
    }

    public List<Payment> getPaymentsByTransaction(int transID)
    {
        List payment = dataAccess.getPaymentsByTransaction(transID);
        return payment;
    }
}