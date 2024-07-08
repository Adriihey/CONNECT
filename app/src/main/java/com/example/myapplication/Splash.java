package com.example.myapplication;

import static android.icu.number.NumberRangeFormatter.with;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mUser != null) {
                    // Get the current user's UID
                    String uid = mUser.getUid();

                    // Reference to Contractor and Client nodes
                    DatabaseReference contractorRef = mRef.child("Contractor").child(uid);
                    DatabaseReference clientRef = mRef.child("Client").child(uid);

                    // Check if the UID exists in the Contractor node
                    contractorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // UID is in Contractor node
                                mRef = FirebaseDatabase.getInstance().getReference().child("Contractor");
                                Intent intent = new Intent(Splash.this,Contractor .class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityWithAnimation(intent);
                                finish();
                            } else {
                                // If not in Contractor, check if the UID exists in the Client node
                                clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // UID is in Client node
                                            mRef = FirebaseDatabase.getInstance().getReference().child("Client");
                                            Intent intent = new Intent(Splash.this, Client.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivityWithAnimation(intent);
                                            finish();
                                        }
                                    }
                                    public void onCancelled(DatabaseError databaseError) {
                                        Intent intent = new Intent(Splash.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivityWithAnimation(intent);
                                        finish();
                                        // Handle possible errors
                                    }
                                });
                            }
                        }
                        public void onCancelled(DatabaseError databaseError) {
                            Intent intent = new Intent(Splash.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityWithAnimation(intent);
                            finish();
                            // Handle possible errors
                        }
                    });
                } else {
                    // Handle the case where no user is signed in
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityWithAnimation(intent);
                    finish();
                }

            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
    private void startActivityWithAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
    }
}