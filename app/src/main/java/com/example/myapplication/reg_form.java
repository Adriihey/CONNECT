package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class reg_form extends AppCompatActivity {

    String [] item = {"Client", "Contractor"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    TextInputEditText editemail, editpassword, editconfirmpass;
    Button regbtn1;
    TextView textView;
    FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_form);
        autoCompleteTextView = findViewById(R.id.autocomplete);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();


            }
        });
        mAuth = FirebaseAuth.getInstance();
        editemail = findViewById(R.id.email);
        editpassword = findViewById(R.id.password);
        editconfirmpass = findViewById(R.id.confirmpass);
        regbtn1 = findViewById(R.id.regsubmit);
        textView = findViewById(R.id.regtologin);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(reg_form.this, LoginFormActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }
        });


        regbtn1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String email, password, confirm, category;
                email = String.valueOf(editemail.getText());
                password = String.valueOf(editpassword.getText());
                confirm = String.valueOf(editconfirmpass.getText());
                category = String.valueOf(autoCompleteTextView.getText());


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm) || TextUtils.isEmpty(category)) {
                    Toast.makeText(reg_form.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirm)) {
                    Toast.makeText(reg_form.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(reg_form.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (category.equals("Client")) {
                    Intent intent = new Intent(reg_form.this, SetupClient.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                } else {
                    Intent intent = new Intent(reg_form.this, SetupContractor.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

            }
        });


    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}