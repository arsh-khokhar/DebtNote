package com.robotraccoons.debtnote.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.business.AccessPayments;
import com.robotraccoons.debtnote.objects.Payment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    AccessPayments payments = new AccessPayments();
    String currUser;
    List<Payment> paymentsList;
    ArrayList<String[]> values = new ArrayList<>();//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        currUser = intent.getStringExtra("user");
        TransactionListAdapter transactionListAdapter = new TransactionListAdapter(this, values);
        ListView transactionList = findViewById(R.id.transactionList);
        transactionList.setAdapter(transactionListAdapter);
        refreshValues();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
              intent.putExtra("user", currUser);
              //System.out.println(currUser + "hello2");
              startActivity(intent);
              finish();
            }
        });
    }

    protected void refreshValues(){
        payments = new AccessPayments();
        paymentsList = payments.getAllPayments(currUser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for(int i = 0; i < paymentsList.size(); i++) {
            String transName = paymentsList.get(i).getTransName();
            String transDescription = paymentsList.get(i).getTransDescription();
            String transID = Integer.toString(paymentsList.get(i).getTransID());
            String[] toAdd = new String[7];
            toAdd[0] = transName;
            toAdd[1] = transDescription;
            toAdd[2] = transID;
            toAdd[3] = currUser;
            double amtOwed = paymentsList.get(i).getAmtOwed() - paymentsList.get(i).getAmtPaid();
            double totalAmt = payments.getTotalAmountOwedByTransaction(Integer.parseInt(transID));
            if(paymentsList.get(i).getTransPayer().equals(currUser)) {
                if(Math.abs(totalAmt - amtOwed) <= 0.01) {
                    toAdd[4] = "You spent";
                    toAdd[5] = "$" + String.format("%.2f",amtOwed);
                    toAdd[6] = ""+Color.BLUE;
                }
                else {
                    toAdd[4] = "You are owed";
                    toAdd[5] = "$" + String.format("%.2f", payments.getRemainingAmountOwedByTransaction(Integer.parseInt(transID)) - amtOwed);
                    toAdd[6] = ""+Color.GREEN;
                }
            }
            else {
                toAdd[4] = "You owe";
                toAdd[5] = "$" + String.format("%.2f", amtOwed);
                toAdd[6] = ""+Color.RED;
            }
            values.add(toAdd);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        values = new ArrayList<>();
        refreshValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, StartupActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}