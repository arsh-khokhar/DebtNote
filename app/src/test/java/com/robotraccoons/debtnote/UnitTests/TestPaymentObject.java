package com.robotraccoons.debtnote.UnitTests;

import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPaymentObject {
    @Test
    public void testAddingPayments() {
        User user1 = new User("user1", "password1");
        Transaction transaction1 = new Transaction(1,"transaction1", "description1", "user1");
        Payment ut = new Payment(user1,transaction1,10,5);
        assertEquals(transaction1.getTransDescription(), ut.getTransDescription());
        assertEquals(transaction1.getTransID(), ut.getTransID());
        assertEquals(transaction1.getTransName(), ut.getTransName());
        assertEquals(user1, ut.getUser());
        assertEquals(user1.getUserName(), ut.getUsername());
        //assertEquals(user1.getUserID(),ut.getUserID());
        assertEquals(10,ut.getAmtOwed(), 0.01);
        assertEquals(5,ut.getAmtPaid(), 0.01);
        ut.addToAmountPaid(5);
        assertEquals(10,ut.getAmtPaid(),0.01);
    }


}