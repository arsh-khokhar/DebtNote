package com.robotraccoons.debtnote.presentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.business.AccessPayments;
import com.robotraccoons.debtnote.business.AccessTransactions;
import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.Payment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionActivity extends AppCompatActivity {
    private MultiAutoCompleteTextView userslist;
    private EditText transName;
    private EditText transDescription;
    private EditText transAmount;
    private Button payerButton;
    private Button splitButton;
    private Button addTransButton;
    private String currUser;
    private String payer;
    List<String> unmodifiableUsernames = new AccessUsers().getUserNames();
    ArrayList<String> allUsernames = new ArrayList<String>(new AccessUsers().getUserNames());
    ArrayList<String> addedUsernames = new ArrayList<String>();
    ArrayList<Double> addedUserShares = new ArrayList<Double>();
    AccessPayments payments = new AccessPayments();
    AccessTransactions transactions = new AccessTransactions();
    List<Payment> paymentList;
    private LinearLayout splitShares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        currUser = intent.getStringExtra("user");
        addedUsernames.add(currUser);
        payer = currUser;
        allUsernames.remove(currUser);
        userslist = (MultiAutoCompleteTextView) findViewById(R.id.newUser);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransactionActivity.this, android.R.layout.simple_list_item_1, allUsernames);
        userslist.setAdapter(adapter);
        userslist.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        userslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getAdapter().getItem(position).toString();
                allUsernames.remove(selected);
                adapter.clear();
                adapter.addAll(allUsernames);
                userslist.setAdapter(adapter);
                //addedUsernames.add(selected);
                updateAddedUsers(userslist.getText().toString());
            }
        });

        userslist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                addedUsernames = updateAddedUsers(userslist.getText().toString());
            }
        });

        paymentList = payments.getAllPayments(currUser);
        transName = (EditText) findViewById(R.id.transactionTitle);
        transDescription = (EditText) findViewById(R.id.transactionDesc);
        transAmount = (EditText) findViewById(R.id.transactionAmount);
        transAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        addTransButton = (Button) findViewById(R.id.addTransButton);
        addTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addedUserShares.size() <= 0) {
                    splitEqually();
                }
                addSharedTransactions(addedUsernames, addedUserShares);

//                Snackbar.make(view, "Saved (...probably)", Snackbar.LENGTH_LONG)
//     s                   .setAction("Action", null).show();

                Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
                intent.putExtra("user", currUser);
                startActivity(intent); //go to main page
                finish();   //close the activity.
            }
        });

        payerButton = (Button) findViewById(R.id.paidByButton);
        payerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedUsernames = updateAddedUsers(userslist.getText().toString());
                Log.d("UPDATED USERS: ", addedUsernames.toString()+"The length is: "+ addedUsernames.size());
                Log.d("AMOUNTS: ", addedUserShares.toString()+"The length is: "+ addedUserShares.size());
                openPayerDialogue();
            }
        });

        splitButton = (Button) findViewById(R.id.splitButton);
        splitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedUsernames = updateAddedUsers(userslist.getText().toString());
                Log.d("UPDATED USERS: ", addedUsernames.toString()+"The length is: "+ addedUsernames.size());
                Log.d("AMOUNTS: ", addedUserShares.toString()+"The length is: "+ addedUserShares.size());
                openSplitDialogue();
            }
        });
    }

    private void openPayerDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionActivity.this);
        builder.setTitle("Choose payer");
        builder.setItems(addedUsernames.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payer = addedUsernames.get(which);
                if(!payer.equals(currUser)) {
                    payerButton.setText(payer);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void openSplitDialogue() {
        splitShares = new LinearLayout(this);
        splitShares.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionActivity.this);
        builder.setTitle("Enter Split amounts");
        for(int i = 0; i < addedUsernames.size(); i++) {
            splitShares.addView(getSplitEditLayout(addedUsernames.get(i), i));
        }
        splitEqually();
        EditText hi = (EditText) splitShares.findViewById(1000); //.findViewById(1000);
        Log.d("HI NULL CHECK", hi+"");
        builder.setView(splitShares);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double total = 0;
                Log.d("ENTERED TEXT", hi.getText().toString());
                for(int i = 0; i < addedUsernames.size(); i++){
                    EditText curr = (EditText) splitShares.findViewById((i+1)*1000);
                    if(curr.getText().toString().trim().length() <= 0) {
                        showErrorDialogue("Please enter all shares");
                        return;
                    }
                    else {
                        double toAdd = Double.parseDouble(curr.getText().toString());
                        addedUserShares.set(i, Double.parseDouble(curr.getText().toString()));
                        total += toAdd;
                    }
                }
                Log.d("TOTAL IS: ", total+"");
                if(!(Math.abs(total - Double.parseDouble(transAmount.getText().toString())) <= 0.01)) {
                    showErrorDialogue("All shares should add up to total amount of transaction");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                splitButton.setText("EQUALLY");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(800, alertDialog.getWindow().getAttributes().height);
        splitButton.setText("UNEQUALLY");
    }

    private void showErrorDialogue(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionActivity.this);
        builder.setTitle("Error");
        builder.setMessage(error);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //openSplitDialogue();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addSharedTransactions(ArrayList<String>addedUsernames, ArrayList<Double> addedUserShares) {
        Log.d("NUM OF TRANSACTIONS: ", ""+transactions.getNumTransactions());
        Log.d("PAYER", payer);
        Transaction toAdd = transactions.insertTransaction(transName.getText().toString(),transDescription.getText().toString(), payer);
        for(int i = 0; i < addedUsernames.size(); i++) {
            payments.insertPayment(addedUsernames.get(i),toAdd, addedUserShares.get(i), 0);
            Log.d("AMT OWED: ",addedUsernames.get(i)+" "+addedUserShares.get(i));
            //addTransaction(addedUsernames.get(i), transName.getText().toString(), transDescription.getText().toString(), addedUserShares.get(i));
        }
    }

    private ArrayList<String> updateAddedUsers(String usersString) {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(currUser);
        String[] toAdd = usersString.split(",");
        //Log.d("ALL Usernames: ",allUsernames.toString());
        //Log.d("USERS CURRENTLY: ",Arrays.toString(toAdd) + "   " +allUsernames.indexOf("matt"));
        for(int i = 0; i < toAdd.length; i++) {
            if(toAdd[i].trim().length() > 0 && unmodifiableUsernames.contains(toAdd[i].trim()) && !toReturn.contains(toAdd[i].trim())) {
                toReturn.add(toAdd[i].trim());
            }
        }
        return toReturn;
    }

    private void splitEqually() {
        double rest = 0.0;
        for(int i = 0; i < addedUsernames.size()-1; i++) {
            //addedUserShares.add(i,0.0);
            if(transAmount.getText().toString().trim().length()>0) {
                double toAdd = Math.round(Double.parseDouble(transAmount.getText().toString())/addedUsernames.size()*100d) / 100d;
                addedUserShares.add(i, toAdd);
                rest+=toAdd;
            }
        }
        if(transAmount.getText().toString().trim().length()>0) {
            //addedUserShares.add(addedUsernames.size()-1,0.0);
            addedUserShares.add(addedUsernames.size()-1,Math.round(Double.parseDouble(transAmount.getText().toString())*100d)/100d-rest);
        }
    }

    private RelativeLayout getSplitEditLayout(String username, int i) {
        RelativeLayout r = new RelativeLayout(this); //Layout(this);
        //r.setId(i+1);
        r.setPadding(20,10,20,10);
        r.setGravity(RelativeLayout.CENTER_VERTICAL);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView newUserText = new TextView(this);
        r.addView(newUserText);
        newUserText.setText(username);
        newUserText.setTextSize(22);
        newUserText.setTextColor(Color.BLACK);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.setMargins(10,22,10,0);
        newUserText.setLayoutParams(lp);
//        newUserText.setId(5);

        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText amt = new EditText(this);
        r.addView(amt);
        amt.setHint("0.00");
        amt.setTextSize(22);
        amt.setTextColor(Color.BLACK);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        amt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        amt.setLayoutParams(lp);
        amt.setId((i+1)*1000);

        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dollar = new TextView(this);
        r.addView(dollar);
        dollar.setText("$");
        dollar.setTextSize(22);
        dollar.setTextColor(Color.BLACK);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.setMargins(10,22,2,0);
        lp.addRule(RelativeLayout.LEFT_OF, amt.getId());
        dollar.setLayoutParams(lp);
        return r;
    }
}