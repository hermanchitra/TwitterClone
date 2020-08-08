package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edtSignupEmail, edtSignupUsername, edtSignupPassword;
    private Button btnSignupSignup, btnSignupLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtSignupEmail = findViewById(R.id.edtSignupEmail);
        edtSignupUsername = findViewById(R.id.edtSignupUsername);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);
        btnSignupSignup = findViewById(R.id.btnSignupSignup);
        btnSignupLogin =findViewById(R.id.btnSignupLogin);

        btnSignupLogin.setOnClickListener(this);
        btnSignupSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignupSignup:
                if (edtSignupEmail.getText().toString().equals("") || edtSignupUsername.getText().toString().equals("") || edtSignupPassword.getText().toString().equals("")) {
                    FancyToast.makeText(SignupActivity.this, "Please fill in email, username and password", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    return;
                }
                ParseUser user = new ParseUser();
                user.setEmail(edtSignupEmail.getText().toString());
                user.setUsername(edtSignupUsername.getText().toString());
                user.setPassword(edtSignupPassword.getText().toString());
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Signing up...");
                dialog.show();
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(SignupActivity.this, "Sign up success", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        } else {
                            FancyToast.makeText(SignupActivity.this, "Sign up failed\n" + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.btnSignupLogin:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}