package com.robotraccoons.debtnote;

import android.support.test.InstrumentationRegistry;
import android.widget.ListView;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.robotraccoons.debtnote.presentation.TransactionListAdapter;


@RunWith(AndroidJUnit4.class)
public class TestTransactionAdapter {

    Context appContext = InstrumentationRegistry.getTargetContext();


    @Test
    public void testNullInputs() {
        ListView transactionList = new ListView(appContext);

        TransactionListAdapter transactionListAdapter = new TransactionListAdapter( null, null);
        transactionList.setAdapter(transactionListAdapter);
    }

    @Test
    public void testEmptyInput() {
        ListView transactionList = new ListView(appContext);
        ArrayList<String[]> values = new ArrayList<String[]>();

        TransactionListAdapter transactionListAdapter = new TransactionListAdapter( appContext, values);
        transactionList.setAdapter(transactionListAdapter);
    }

    @Test
    public void testNormalInputs() {
        ListView transactionList = new ListView(appContext);
        ArrayList<String[]> values = new ArrayList<String[]>();
        for(int i = 0; i < 25; i++){
            String[] toTest = new String[2];
            toTest[0] = "Transaction ID" + i;
            toTest[0] = "Transaction name " + i;
            values.add(toTest);

        }

        TransactionListAdapter transactionListAdapter = new TransactionListAdapter( appContext, values);
        transactionList.setAdapter(transactionListAdapter);
    }

}
