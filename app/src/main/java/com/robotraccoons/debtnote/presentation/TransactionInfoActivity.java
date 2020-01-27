package com.robotraccoons.debtnote.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.business.AccessPayments;
import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;

public class TransactionInfoActivity extends AppCompatActivity {
    AccessTransactions transactions = new AccessTransactions();
    AccessPayments payments = new AccessPayments();
    private String currUser;
    private int currTrans;
    private String currPayer;
    private LinearLayout splitDetails;
    private List<Payment> paymentsForCurrTrans; // = payments.getPaymentsByTransaction(currTrans);
    NumberFormat formatter;
    LinearLayout.LayoutParams layoutParams;
    EditText payingAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_info2);
        Intent intent = getIntent();
        currTrans = Integer.parseInt(intent.getStringExtra("transaction"));
        currUser = intent.getStringExtra("user");
        formatter = NumberFormat.getCurrencyInstance();

        String amountFull = formatter.format(payments.getTotalAmountOwedByTransaction(currTrans));
        String amountOwed = formatter.format(payments.getUserOwedAmt(currTrans, currUser));

        Transaction trans = transactions.getTransactionById(currTrans);
        currPayer = trans.getPayer();
        Log.d("PAYER: ", trans.getPayer());
        TextView title = (TextView) findViewById(R.id.transactionTitle);
        title.setText(trans.getTransName());

        TextView desc = (TextView) findViewById(R.id.transactionDesc);
        desc.setText(trans.getTransDescription());

        TextView amtFull = (TextView) findViewById(R.id.transactionAmount);
        amtFull.setText(amountFull);

        splitDetails = (LinearLayout) findViewById(R.id.splitDetails);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.setMargins(10, 10, 10, 10);

        TextView payer = new TextView(this);
        payer.setTextSize(22);
        payer.setText(currPayer + " paid " + amountFull + " and owes " + formatter.format(payments.getUserOwedAmt(currTrans,currPayer)));
        payer.setLayoutParams(layoutParams);
        splitDetails.addView(payer);

        inflateDetails();

        payingAmount = (EditText) findViewById(R.id.transactionAmount2);
        payingAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        ImageButton del = findViewById(R.id.deleteButton);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTransaction(currTrans);
                Intent intent = new Intent(TransactionInfoActivity.this, MainActivity.class);
                intent.putExtra("user", currUser);
                startActivity(intent); //go to main page
                finish();
            }
        });
        Button button2 = findViewById(R.id.paymentButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payingAmount.getText().toString().trim().length()>0) {
                    double payingAmtDouble = Double.parseDouble(payingAmount.getText().toString());
                    if(payingAmtDouble > payments.getUserOwedAmt(currTrans, currUser)) {
                        showErrorDialogue("Please enter an amount less than you owe!");
                        return;
                    }
                    payments.payToTransaction(currUser, currTrans, payingAmtDouble);
                    System.out.println("AAAAAAAAAAA- Adding to transaction here: " + payingAmtDouble);
                }
                Intent intent = new Intent(TransactionInfoActivity.this, MainActivity.class);
                intent.putExtra("user", currUser);
                startActivity(intent);
                finish();
            }
        });
        if(currUser.equals(currPayer)) {
            findViewById(R.id.transactionTitle2).setVisibility(View.INVISIBLE);
            payingAmount.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
        }
    }

    private void deleteTransaction(Integer transactionId) {
        transactions.deleteTransactionById(transactionId);
        payments.deleteTransactionPayments(transactionId);
    }

    private void inflateDetails() {
        paymentsForCurrTrans = payments.getPaymentsByTransaction(currTrans);
        for(int i = 0; i < paymentsForCurrTrans.size(); i++) {
            if(!paymentsForCurrTrans.get(i).getUsername().equals(currPayer)) {
                String user = paymentsForCurrTrans.get(i).getUsername();
                String amt = formatter.format(payments.getUserOwedAmt(currTrans, user));
                TextView newDetail = new TextView(this);
                newDetail.setTextSize(22);
                newDetail.setText(user + " owes " + amt);
                newDetail.setLayoutParams(layoutParams);
                splitDetails.addView(newDetail);
            }
        }
    }

    private void showErrorDialogue(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionInfoActivity.this);
        builder.setTitle("Error");
        builder.setMessage(error);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
