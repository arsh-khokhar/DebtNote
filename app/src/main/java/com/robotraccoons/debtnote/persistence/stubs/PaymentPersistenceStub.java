package com.robotraccoons.debtnote.persistence.stubs;
import android.util.Log;

import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.persistence.PaymentPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentPersistenceStub implements PaymentPersistence {
    private AccessTransactions transactions = new AccessTransactions();
    private List<Payment> payments;
    private AccessUsers users = new AccessUsers();

    List<User> userList = users.getUsers();
    public PaymentPersistenceStub() {
        this.payments = new ArrayList<>();
        final Transaction t1 = transactions.getTransactionById(1);
        final User user1 = new User("marker", "himarker");
        this.payments.add(new Payment(user1, t1, 20, 0));
    }

    @Override
    public List<Payment> getPaymentsByUsername(String username) {
        List<Payment> newPayments = new ArrayList<Payment>();;
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getUsername().equals(username))
            {
                newPayments.add(payments.get(i));
            }
        }
        return newPayments;
    }

    public List<Payment> getAllPayments()
    {
        return Collections.unmodifiableList(payments);
    }

    public void payToTransaction(final String username, final int tId, final double amount ){
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getTransID() == tId && payments.get(i).getUser().getUserName().equals(username))
            {
                payments.get(i).addToAmountPaid(amount);
            }
        }
    }

    @Override
    public List<Payment> getPaymentsByTransaction(int transID) {
        //Log.d("Payment ID: ", ""+payments.get(0).getTransID());
        List<Payment> newTransactionUsers = new ArrayList<Payment>();;
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getTransID() == transID)
            {
                //Log.d("THIS IS TRUE!", "for "+ payments.get(i).getUsername());
                newTransactionUsers.add(payments.get(i));
            }
        }
        //Log.d("Size after: ", newTransactionUsers.size()+"");
        return newTransactionUsers;
    }


    @Override
    public Payment insertPayment(String username, Transaction currentPayment, double amtOwed, double amtPaid) {
        Payment toInsert = null;
        amtOwed = (double)Math.round(amtOwed * 100d) / 100d;
        amtPaid = (double)Math.round(amtPaid * 100d) / 100d;
        for (int i = 0; i < userList.size(); i++)
        {
            if (userList.get(i).getUserName().equals(username))
            {
                toInsert = new Payment(userList.get(i), currentPayment, amtOwed, amtPaid);
                break;
            }
        }
        if(toInsert!=null) {
            payments.add(toInsert);
        }
        return toInsert;
    }

    @Override
    public double getRemainingAmountOwedByTransaction(final int id) {
        double amountOwed = getTotalAmountOwedByTransaction(id);
        double amountPaid = 0.0;
        double amountToReturn = 0.0;
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getTransID() == id)
            {
                amountPaid +=  payments.get(i).getAmtPaid();
            }
        }
        amountToReturn = (double)Math.round((amountOwed - amountPaid) * 100d) / 100d;
        return amountToReturn;
    }

    @Override
    public double getTotalAmountOwedByTransaction(final int id){
        double amountToReturn = 0;
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getTransID() == id)
            {
                amountToReturn +=  payments.get(i).getAmtOwed();
            }
        }
        amountToReturn = (double)Math.round(amountToReturn * 100d) / 100d;
        return amountToReturn;
    }

    @Override
    public void deleteTransactionPayments(final int transID){
        int i = 0;
        while (i < payments.size())
        {
            if (payments.get(i).getTransID() == transID)
            {
                payments.remove(payments.get(i));
                i = 0;
            }else{
                i++;
            }
        }
    }

    @Override
    public double getUserOwedAmt(final int id, final String username) {
        double amountToReturn = 0;
        for (int i = 0; i < payments.size(); i++)
        {
            if (payments.get(i).getTransID() == id && payments.get(i).getUsername().equals(username))
            {
                amountToReturn +=  payments.get(i).getAmtOwed() - payments.get(i).getAmtPaid();
            }
        }
        amountToReturn = (double)Math.round(amountToReturn * 100d) / 100d;
        return amountToReturn;
    }
}