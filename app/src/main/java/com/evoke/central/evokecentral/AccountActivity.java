package com.evoke.central.evokecentral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {

    private Button mLoginBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageView imageView;
    private TextView uname, email, userId, verif;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        imageView = (ImageView) findViewById(R.id.displayPhoto);
        uname = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        userId = (TextView) findViewById(R.id.userId);
        verif = (TextView) findViewById(R.id.verifstatus);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Picasso.with(AccountActivity.this).load(user.getPhotoUrl()).placeholder(R.drawable.person).into(imageView);
        email.setText("Email: " + user.getEmail());
        if (user.getDisplayName() != null) {
            uname.setText("Username: " + user.getDisplayName());
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            ValueEventListener eventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String display_name = (String) dataSnapshot.child("displayname").getValue();
                    uname.setText("Username: " + display_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            ref.addListenerForSingleValueEvent(eventListener);
        }
        userId.setText("User id: " + user.getUid());

        if (user.isEmailVerified() == false) {
            verif.setText("Verification status: Unverified, please veify your email");
        } else {
            verif.setText("Verification status: Verified");
        }

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){


                    startActivity(new Intent(AccountActivity.this, MainActivity.class));

                }


            }
        };

        mLoginBtn = (Button) findViewById(R.id.button2);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(AccountActivity.this);
                pd.setMessage("Signing out...");
                pd.setCancelable(false);
                pd.show();
                mAuth.signOut();
                pd.dismiss();
            }
        });
    }

    @Override
    protected void onStart(){

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }

}