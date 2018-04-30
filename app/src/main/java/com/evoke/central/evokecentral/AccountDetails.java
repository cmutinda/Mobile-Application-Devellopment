package com.evoke.central.evokecentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountDetails extends AppCompatActivity {
    EditText mFirstName;
    EditText mSurname;
    EditText mDisplayName;
    Button mRegister2;

    private String userID;

    private DatabaseReference mDatabase;

    String fname, lname, dname, email;

    ProgressDialog pDialog;

    private static final String TAG = "AddToDatabase";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        mRegister2 = (Button) findViewById(R.id.mRegister2);
        mFirstName = (EditText) findViewById(R.id.mFirstName);
        mSurname = (EditText) findViewById(R.id.mSurname);
        mDisplayName = (EditText) findViewById(R.id.mDisplayName);

        final String email = getIntent().getExtras().getString("email");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: Added information to database: \n" +
                        dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        mRegister2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(AccountDetails.this);
                pDialog.setMessage("Updating details...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();




                Log.d(TAG, "onClick: Submit pressed.");
                String name = mFirstName.getText().toString();
                String surname = mSurname.getText().toString();
                String displayname = mDisplayName.getText().toString();

                Log.d(TAG, "onClick: Attempting to submit to database: \n" +
                        "first_name: " + name + "\n" +
                        "surname: " + surname + "\n" +
                        "display_name: " + displayname + "\n" +
                        "email: " + email + "\n"
                );

                if (!name.equals("") && !surname.equals("") && !displayname.equals("")) {
                    UserInformation userInformation = new UserInformation(name, surname, displayname, email);
                    myRef.child("users").child(userID).setValue(userInformation);
                    toastMessage("Details updated successfully!");
                    mFirstName.setText("");
                    mSurname.setText("");
                    mDisplayName.setText("");
                    pDialog.hide();
                    toastMessage("Welcome to Evoke Central " + displayname + " Please check your email and verify your account...");
                } else {
                    toastMessage("Fill out all the fields to proceed!");
                }

                Intent intent = new Intent(AccountDetails.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    sendVerificationEmail();

                } else {
                }
            }
        };

    }

    private void sendVerificationEmail() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final String email = getIntent().getExtras().getString("email");
                            Toast.makeText(AccountDetails.this, "Verification Email sent to " + email + " please verify your account...", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });


    }


    @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
