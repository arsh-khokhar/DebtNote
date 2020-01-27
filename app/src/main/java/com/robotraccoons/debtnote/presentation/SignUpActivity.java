package com.robotraccoons.debtnote.presentation;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.User;

import java.sql.SQLOutput;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpBtn;
    AccessUsers users = new AccessUsers();
    List<User> userlist  = users.getUsers();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                userName = (EditText) findViewById(R.id.signUpNameET);               //find the edit text(text box)
                password = (EditText) findViewById(R.id.signUpPasswordET);
                confirmPassword = (EditText) findViewById(R.id.signUpConfirmPasswordET);
                if(!validateUsername(userName.getText().toString()) || !validatePassword(password.getText().toString())) {
                    if(!validateUsername(userName.getText().toString())) {
                        userName.setText("");
                    }
                    if(!validatePassword(password.getText().toString())) {
                        password.setText("");
                        confirmPassword.setText("");
                    }
                }
                else if(validateSignUpInfo()){
                    Toast.makeText(getApplicationContext(), "Sign up was successful! Proceed to login", Toast.LENGTH_SHORT).show();
                    onBackPressed();           //return previous activity
                    addUser(userName.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    //add user info to data
    private void addUser(String username, String password){
        users.insertUser(new User(username, password));//, userlist.size()+1));
    }

    private boolean validateUsername(String inputUsername) {
        boolean isValid = true;
        String errorMsg = null;
        if(inputUsername.length()==0) {
            userName.setError("Username is required!");
            return false;
        }
        if(!inputUsername.matches("[a-zA-Z0-9]*")) {
            errorMsg ="Username should only contain alphabets and letters.";
            isValid = false;
        }
        if(inputUsername.length()<4) {
            errorMsg+="\nUsername should be at least 4 characters long.";
            isValid = false;
        }
        userName.setError(errorMsg);
        return isValid;
    }

    private boolean validatePassword(String inputPassword) {
        boolean isValid = true;
        String errorMsg = null;
        if(inputPassword.length()==0) {
            password.setError("Password is required!");
            return false;
        }
        if(inputPassword.length()< 6) {
            errorMsg ="Password should be at least 6 characters long";
            isValid = false;
        }
        password.setError(errorMsg);
        return isValid;
    }

    //validate sign-Up info
    private boolean validateSignUpInfo(){

        boolean isValid = true;

        if(users.hasUser(userName.getText().toString())) {
            showError("Invalid username","This username is already exists!");
            isValid = false;
        }

        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            showError("Password error","Passwords do not match!");
            password.setText("");
            confirmPassword.setText("");
            isValid = false;
        }
        return isValid;
    }

    private void showError(String errorTitle, String errorMessage) {
        AlertDialog.Builder prompt = new AlertDialog.Builder(SignUpActivity.this);
        prompt.setTitle(errorTitle);
        prompt.setMessage(errorMessage);
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Please retry", Toast.LENGTH_SHORT).show();
            }
        });
        prompt.show();
    }
}