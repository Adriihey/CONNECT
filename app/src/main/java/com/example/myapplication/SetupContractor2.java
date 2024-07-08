package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetupContractor2 extends AppCompatActivity {

    AppCompatButton confirm, back;
    EditText contact, pcab, page;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_contractor2);
        confirm = findViewById(R.id.v2confirm);
        contact = findViewById(R.id.num);
        pcab = findViewById(R.id.pcab);
        page = findViewById(R.id.link);
        mAuth = FirebaseAuth.getInstance();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number, license, pagelink;
                number = contact.getText().toString().trim();
                license = pcab.getText().toString().trim();
                pagelink = page.getText().toString().trim();


                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(license) || TextUtils.isEmpty(pagelink)) {
                    Toast.makeText(SetupContractor2.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    HashMap<String, Object> v1 = new HashMap<>();
                    v1.put("license", license);
                    v1.put("link", pagelink);
                    v1.put("phone", number);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference;
                    reference = database.getReference("Contractor");
                    reference.child(user.getUid()).updateChildren(v1);
                    Toast.makeText(SetupContractor2.this, "Account Created",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SetupContractor2.this, LoginFormActivity.class));
                    finish();
                }
            }
        });


    }

    @Override
    public void onBackPressed(){}
}