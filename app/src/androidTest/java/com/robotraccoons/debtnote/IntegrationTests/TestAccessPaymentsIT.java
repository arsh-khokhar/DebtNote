package com.robotraccoons.debtnote.IntegrationTests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.robotraccoons.debtnote.business.AccessPayments;
import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TestAccessPaymentsIT {
    Context appContext = InstrumentationRegistry.getTargetContext();
    AccessPayments ut = new AccessPayments();
    AccessTransactions utr = new AccessTransactions();
    AccessUsers usrs = new AccessUsers();
    @Test
    public void testAccess() {
        assertTrue(ut.getAllPayments("dummy").size()==0);
        User user = new User(0, "marker", "himarker");
        User user2 = new User(1, "notmarker", "hinotmarker");
        usrs.insertUser(user);
        usrs.insertUser(user2);
    }

    @Test
    public void testInsert() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1, "name","desc", "marker");
        Payment p = ut.insertPayment("notmarker", testTrans, 50, 0);
        assertNotNull(p);
        int sizeToTest = ut.getAllPayments("marker").size();
        assertTrue(sizeToTest > 0);
        ut.insertPayment("notmarker", testTrans, 50, 30);
        assertEquals(sizeToTest+1, ut.getAllPayments("marker").size());
    }

    @Test
    public void testGetTotalAmountOwedByTransaction() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 50, 0) ;
        assertEquals(50, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()), 0.01);
        ut.insertPayment("marker", testTrans, 50, 0);
        assertEquals(100, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()), 0.01);
    }

    @Test
    public void testMakePayment() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 50, 0);
        ut.payToTransaction("marker", testTrans.getTransID(), 10);
        assertEquals(ut.getRemainingAmountOwedByTransaction(testTrans.getTransID()), 40, 0.01);
        ut.payToTransaction("marker", testTrans.getTransID(), 20.5);
        assertEquals(19.5, ut.getRemainingAmountOwedByTransaction(testTrans.getTransID()), 0.01);
    }

    @Test
    public void testGetRemaningAmountOwedByTransaction() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        Payment p = ut.insertPayment("marker", testTrans, 50, 0);
        assertNotNull(p);
        assertEquals(ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),50, 0.01);
        ut.insertPayment("marker2", testTrans, 50, 0);
        assertEquals(ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),100, 0.01);
    }

}