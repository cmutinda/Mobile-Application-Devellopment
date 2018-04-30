package com.evoke.central.evokecentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Registration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText mFirstName;
    EditText mSurname;
    EditText mDisplayName;
    EditText mEMailAddress;
    EditText mPassword;
    EditText mConfirmPassword;
    Button mRegister;

    private DatabaseReference mDatabase;


    String  email, password, confirmpassword;

    ProgressDialog pDialog;



    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        mFirstName = (EditText) findViewById(R.id.mFirstName);
        mSurname = (EditText) findViewById(R.id.mSurname);
        mDisplayName = (EditText) findViewById(R.id.mDisplayName);
        mEMailAddress = (EditText) findViewById(R.id.mRegisterEmail);
        mPassword = (EditText) findViewById(R.id.mRegisterPassword);
        mConfirmPassword = (EditText) findViewById(R.id.mConfirmPassword);
        mRegister = (Button) findViewById(R.id.mRegister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegisterDetails();
            }
        });

        
    }


    private void validateRegisterDetails() {
        email = mEMailAddress.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        confirmpassword = mConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEMailAddress.setError("Please Enter Email Address");
        } else if (TextUtils.isEmpty(password)){
            mPassword.setError("Please Enter Your Password");
        } else if (!password.equals(confirmpassword)) {
            mConfirmPassword.setError("Passwords do not match ! Re-enter Passwords");
        }else {
            registerUser();
        }

    }

    private void registerUser() {
        pDialog = new ProgressDialog(Registration.this);
        pDialog.setMessage("Signing Up...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        //User Creation
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        pDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "User Registered Successfully! Please update your details...", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Registration.this, AccountDetails.class);
                            String email = mEMailAddress.getText().toString();
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Registration.this, "User Registration Failed! Email associated with another account or you are not connected to the internet!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
