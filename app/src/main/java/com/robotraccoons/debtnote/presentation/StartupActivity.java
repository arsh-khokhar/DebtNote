package com.robotraccoons.debtnote.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.SQLite.DatabaseSingleton;

import java.util.List;

public class StartupActivity extends AppCompatActivity {
    private Button loginButton;
    private Button signupButton;
    AccessUsers users = new AccessUsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);


        loginButton = (Button) findViewById(R.id.loginbutton);
        signupButton = (Button) findViewById(R.id.signupbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameET = (EditText) findViewById(R.id.logInUserNameET);
                EditText passwordET = (EditText) findViewById(R.id.logInPasswordET);
                if(validateLogIn(userNameET.getText().toString(), passwordET.getText().toString())){     //if user name and password is valid
                    Intent intent = new Intent(StartupActivity.this, MainActivity.class);
                    intent.putExtra("user", userNameET.getText().toString());
                    startActivity(intent); //go to main page
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    finish();   //close the activity.
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupPage();
            }
        });
    }

    public void openSignupPage() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //check the Log-in name and password from database
    private boolean validateLogIn(String userName, String password){
        boolean isValid = users.validateUserLogin(userName, password);
        if(!isValid){
            showError("Invalid Credentials", "Username or password is incorrect!");
        }
        return isValid;
    }

    private void showError(String errorTitle, String errorMessage) {
        AlertDialog.Builder prompt = new AlertDialog.Builder(StartupActivity.this);
        prompt.setTitle(errorTitle);
        prompt.setMessage(errorMessage);
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Please retry!", Toast.LENGTH_SHORT).show();
            }
        });
        prompt.show();
    }
}