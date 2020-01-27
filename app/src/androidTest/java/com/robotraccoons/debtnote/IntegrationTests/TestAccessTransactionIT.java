package com.robotraccoons.debtnote.IntegrationTests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.objects.Transaction;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class TestAccessTransactionIT {
    AccessTransactions ut = new AccessTransactions();
    Context appContext = InstrumentationRegistry.getTargetContext();

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
        assertEquals(transaction1.getTransID() + 1, transaction2.getTransID());
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
