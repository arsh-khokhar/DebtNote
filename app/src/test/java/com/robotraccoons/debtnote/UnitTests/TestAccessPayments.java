package com.robotraccoons.debtnote.UnitTests;

import com.robotraccoons.debtnote.business.AccessPayments;
import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.persistence.PaymentPersistence;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class TestAccessPayments {
    private PaymentPersistence ppmock = mock(PaymentPersistence.class);
    private TransactionPersistence tpmock = mock(TransactionPersistence.class);
    private AccessPayments ut = new AccessPayments(ppmock);
    private AccessTransactions utr = new AccessTransactions(tpmock);

    @Test
    public void testAccess() {
        assertEquals(0, ut.getAllPayments("dummy").size());
    }

    @Test
    public void testInsert() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1, "name","desc", "marker");
        ut.insertPayment("marker", testTrans, 50, 0);
        int sizeToTest = ut.getAllPayments("marker").size();
        assertTrue(sizeToTest >= 0);
        ut.insertPayment("marker", testTrans, 50, 30);
        assertEquals(0, ut.getAllPayments("marker").size());
    }

    @Test
    public void testGetTotalAmountOwedByTransaction() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 50, 0);
        assertEquals(0, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),0.01);
        ut.insertPayment("marker", testTrans, 50, 0);
        assertEquals(0, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),0.01);
    }

    @Test
    public void testMakePayment() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 50, 0);
        ut.payToTransaction("marker", testTrans.getTransID(), 10);
        assertEquals(0, ut.getRemainingAmountOwedByTransaction(testTrans.getTransID()), 0.01);
        ut.payToTransaction("marker", testTrans.getTransID(), 20.5);
        assertEquals(0, ut.getRemainingAmountOwedByTransaction(testTrans.getTransID()), 0.01);
    }

    @Test
    public void testGetRemainingAmountOwedByTransaction() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 0, 0);
        assertEquals(0, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),0.01);
        ut.insertPayment("marker", testTrans, 0, 0);
        assertEquals(0, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),0.01);
    }

    @Test
    public void testDeleteTransactionPayments() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 0, 0);
        assertEquals(0, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),0.01);
        ut.deleteTransactionPayments(testTrans.getTransID());
        assertEquals(0, ut.getPaymentsByTransaction(testTrans.getTransID()).size());
    }

    @Test
    public void testGetUserOwedAmt() {
        int numTransactions = utr.getNumTransactions();
        Transaction testTrans = new Transaction(numTransactions+1,"name","desc", "marker");
        ut.insertPayment("marker", testTrans, 0, 0);
        assertEquals(0, ut.getTotalAmountOwedByTransaction(testTrans.getTransID()),0.01);
        assertEquals(0, ut.getUserOwedAmt(testTrans.getTransID(), "marker"), 0.01);
    }
//
//    public double getUserOwedAmt(final int id, final String username) {
//        return dataAccess.getUserOwedAmt(id,username);
//    }
//
//    public List<Payment> getPaymentsByTransaction(int transID) {
//        return dataAccess.getPaymentsByTransaction(transID);
//    }
//}
}