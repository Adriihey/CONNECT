package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClientProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    String profileUri, username,jemail, jlink, jloc, jphone, jdistrict, jbudget, jtype, jscope, jname;
    CircleImageView profileImageHeader, profilephoto;
    TextView usernameHeader, location, number, address, link, dstrct, budget, scope, type, name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("PROFILE");

        // Set font size, family, and color directly in the TextView
        customTitle.setTextSize(20); // Font size in sp
        customTitle.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL)); // Font family
        customTitle.setTextColor(ContextCompat.getColor(this, R.color.white)); // Text color

        // Set layout parameters for the custom title
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL
        );
        customTitle.setLayoutParams(layoutParams);

        // Set the custom title view
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(customTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Client");
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        profilephoto = findViewById(R.id.profile_image);
        location= findViewById(R.id.location);
        number= findViewById(R.id.number);
        address= findViewById(R.id.email);
        link = findViewById(R.id.link);
        budget = findViewById(R.id.userlink);
        scope = findViewById(R.id.scope);
        type = findViewById(R.id.buiding);
        dstrct = findViewById(R.id.district);
        name = findViewById(R.id.clientname);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileImageHeader=view.findViewById(R.id.profileImage_header);
        usernameHeader = view.findViewById(R.id.username_header);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser==null){
            SendUserToLogin();
        }
        else {
            mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        profileUri = snapshot.child("image").getValue().toString();
                        username = snapshot.child("fullname").getValue().toString();
                        jemail = snapshot.child("email").getValue().toString();
                        jlink = snapshot.child("fb_link").getValue().toString();
                        jloc = snapshot.child("location").getValue().toString();
                        jphone = snapshot.child("phone").getValue().toString();
                        jdistrict = snapshot.child("district").getValue().toString();
                        jbudget = snapshot.child("budget").getValue().toString();
                        jtype = snapshot.child("building").getValue().toString();
                        jscope = snapshot.child("work").getValue().toString();
                        jname = snapshot.child("fullname").getValue().toString();
                        Picasso.get().load(profileUri).into(profileImageHeader);
                        Picasso.get().load(profileUri).into(profilephoto);
                        usernameHeader.setText(username);
                        number.setText(jphone);
                        address.setText(jemail);
                        link.setText(jlink);
                        dstrct.setText(jdistrict);
                        location.setText(jloc);
                        budget.setText(jbudget);
                        type.setText(jtype);
                        scope.setText(jscope);
                        name.setText(jname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });
        }
    }

    private void SendUserToLogin() {
        Intent intent = new Intent(ClientProfile.this, LoginFormActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(ClientProfile.this, Client.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(ClientProfile.this, ClientProfile.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(ClientProfile.this, ClientConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(ClientProfile.this, MainActivity.class);
            startActivityWithAnimation(intent);


        }

        return false;
    }

    private void startActivityWithAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

}