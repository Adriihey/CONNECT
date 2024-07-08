package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button gologin, goreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gologin = findViewById(R.id.flogin);
        goreg = findViewById(R.id.freg);

        gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(MainActivity.this, LoginFormActivity.class);
                ActivityOptionsCompat compat1 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.logo1), "trans1");
                startActivity(intent1, compat1.toBundle());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }

        });
        gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, LoginFormActivity.class);
                ActivityOptionsCompat compat2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.card), "trans2");
                startActivity(intent2, compat2.toBundle());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }

        });

        goreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, reg_form.class);
                ActivityOptionsCompat compat4 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.logo1), "trans1");
                startActivity(intent4, compat4.toBundle());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }
        });

        goreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, reg_form.class);
                ActivityOptionsCompat compat3 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.card), "trans2");
                startActivity(intent3, compat3.toBundle());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });


    }
}