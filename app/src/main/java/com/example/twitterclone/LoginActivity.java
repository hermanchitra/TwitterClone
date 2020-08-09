package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtLoginUsername, edtLoginPassword;
    private Button btnLoginSignup, btnLoginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginSignup = findViewById(R.id.btnLoginSignup);
        btnLoginLogin =findViewById(R.id.btnLoginLogin);

        btnLoginLogin.setOnClickListener(this);
        btnLoginSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLoginLogin:
                if (edtLoginUsername.getText().toString().equals("") || edtLoginPassword.getText().toString().equals("")) {
                    FancyToast.makeText(LoginActivity.this, "Please fill in email, username and password", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    return;
                }
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Logging in...");
                dialog.show();
                ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            FancyToast.makeText(LoginActivity.this, "Login success", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            Intent intent = new Intent(LoginActivity.this, TwitterUsers.class);
                            startActivity(intent);
                            finish();
                        } else {
                            FancyToast.makeText(LoginActivity.this, "Login failed\n" + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.btnLoginSignup:
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }
}