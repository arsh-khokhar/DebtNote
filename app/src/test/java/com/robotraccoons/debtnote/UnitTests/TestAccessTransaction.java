package com.robotraccoons.debtnote.UnitTests;

import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAccessTransaction {
    private TransactionPersistence tpmock = mock(TransactionPersistence.class);

    private AccessTransactions ut = new AccessTransactions(tpmock);

    @Before
    public void setup(){
        when(tpmock.insertTransaction(anyString(), anyString(), anyString())).thenReturn(
                new Transaction(10,"man", "hi man", "me")
        );
    }

    @Test
    public void testAccess() {
        assertNull(ut.getTransactionById(1019));
    }

    @Test
    public void testInsert() {
        Transaction transaction1 = ut.insertTransaction("man","hi man", "me");
        Transaction transaction2 = ut.insertTransaction("man2","hi man2", "me");
        assertNotNull(transaction1);
        assertNotNull(transaction2);
        assertEquals(transaction1.getTransID(), transaction2.getTransID());
        assertEquals( transaction1.getTransName(),"man");
        assertEquals( transaction1.getTransDescription(),"hi man");
        assertEquals( transaction1.getPayer(), "me");

    }

    @Test
    public void testDeleteById() {
        Transaction transaction3 = ut.insertTransaction("man3", "hi man3", "me");
        ut.deleteTransactionById(transaction3.getTransID());
        assertNull(ut.getTransactionById(transaction3.getTransID()));
    }
}
