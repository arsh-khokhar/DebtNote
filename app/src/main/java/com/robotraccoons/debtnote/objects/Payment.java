package com.robotraccoons.debtnote.objects;

public class Payment {
    private static int numPayment= 0;
    private final int paymentID;
    private final User user;
    private final Transaction transaction;
    private final double amtOwed;
    private double amtPaid;

    public Payment(final int paymentID, final User user, final Transaction transaction, final double amtOwed, final double amtPaid) {
        this.paymentID = paymentID;
        this.user = user;
        this.transaction = transaction;
        this.amtOwed = amtOwed;
        this.amtPaid = amtPaid;
    }

    public Payment( final User user, final Transaction transaction, final double amtOwed, final double amtPaid) {
        this.paymentID =  numPayment;
        numPayment++;
        this.user = user;
        this.transaction = transaction;
        this.amtOwed = amtOwed;
        this.amtPaid = amtPaid;
    }

    public int getPaymentID(){ return paymentID; }

    //public int getUserID() {
    //    return (user.getUserID());
    //}

    public User getUser() {
        return user;
    }
    public double getAmtOwed() {
        return amtOwed;
    }

    public double getAmtPaid() {
        return amtPaid;
    }

    public String getUsername() {
        return (user.getUserName());
    }

    public int getTransID() {
        return (transaction.getTransID());
    }

    public String getTransName() {
        return (transaction.getTransName());
    }

    public String getTransDescription() {
        return (transaction.getTransDescription());
    }

    public String getTransPayer(){ return transaction.getPayer();}

    public void addToAmountPaid(final double amount) {
        if(amount <= this.amtOwed - this.amtPaid){
            this.amtPaid += amount;
        }else {
            this.amtPaid = this.amtOwed;
        }

    }

}
