package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFormActivity extends AppCompatActivity {
    TextInputEditText editemail, editpassword;
    Button loginbtn1;
    TextView textView, recover;

    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        mAuth = FirebaseAuth.getInstance();
        editemail = findViewById(R.id.email);
        editpassword = findViewById(R.id.password);
        loginbtn1 = findViewById(R.id.loginsubmit);
        textView = findViewById(R.id.logintoreg);
        recover = findViewById(R.id.passforgot);


        loginbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editemail.getText().toString();
                String password = editpassword.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editemail.setError("Invalid Email");
                    editemail.setFocusable(true);
                }
                else {
                    loginUser(email, password);
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginFormActivity.this, reg_form.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }

        });

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);
        final EditText recoveremail = new EditText(this);
        recoveremail.setHint("Enter email");
        recoveremail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        recoveremail.setMinEms(16);

        linearLayout.addView(recoveremail);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = recoveremail.getText().toString().trim();
                beginRecovery(email);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.create().show();
    }

    private void beginRecovery(String email) {
        pd.show();
        LottieAnimationView animationView = new LottieAnimationView(LoginFormActivity.this);
        animationView.setAnimation(R.raw.pd);
        animationView.loop(true); // Set loop to true
        animationView.playAnimation();
        pd.setContentView(animationView);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginFormActivity.this, "Password recovery email sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginFormActivity.this, "Password recovery email sending failed", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginFormActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loginUser(String email, String passw) {
        if (email.isEmpty() || passw.isEmpty()) {
            // Email or password is empty, show an error message
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        pd.show();
        LottieAnimationView animationView = new LottieAnimationView(LoginFormActivity.this);
        animationView.setAnimation(R.raw.pd);
        animationView.loop(true); // Set loop to true
        animationView.playAnimation();
        pd.setContentView(animationView);
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            checkUserCategory(uid);
                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginFormActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(LoginFormActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkUserCategory(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        userRef.child("Client").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pd.dismiss();
                    startActivity(new Intent(LoginFormActivity.this, Client.class));
                    finish(); // Optional: Finish the current activity if you don't want the user to come back here after logging in
                } else {
                    userRef.child("Contractor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                pd.dismiss();
                                startActivity(new Intent(LoginFormActivity.this, Contractor.class));
                                finish(); // Optional
                            } else {
                                // If the user is not found in both nodes, show an error message
                                pd.dismiss();
                                Toast.makeText(LoginFormActivity.this, "User not found in Client or Contractor category", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            pd.dismiss();
                            Toast.makeText(LoginFormActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                Toast.makeText(LoginFormActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}