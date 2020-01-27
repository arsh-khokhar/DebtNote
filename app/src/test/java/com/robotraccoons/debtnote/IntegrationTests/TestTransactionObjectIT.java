package com.robotraccoons.debtnote.IntegrationTests;

import com.robotraccoons.debtnote.objects.Transaction;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTransactionObjectIT {
    @Test
    public void testAddingTransactions() {
        Transaction transaction1 = new Transaction(1,"transaction1", "description1","user1");
        assertEquals("transaction1",transaction1.getTransName());
        assertEquals("description1",transaction1.getTransDescription());
        Transaction transaction2 = new Transaction(2,"transaction2", "description2", "user1");
        assertEquals(transaction1.getTransID()+1, transaction2.getTransID());
    }
}