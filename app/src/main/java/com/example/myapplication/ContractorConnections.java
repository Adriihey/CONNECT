package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.Utills.ContractorConnectionsHolder;
import com.example.myapplication.Utills.ClientUsers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContractorConnections extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseRecyclerOptions<ContractorConnectionsHolder>options;
    FirebaseRecyclerAdapter<ContractorConnectionsHolder, ConnectContractor>adapter;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef, mUserRef1, mUserRef2;
    String profileUri, username;
    CircleImageView profileImageHeader;
    TextView usernameHeader;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_connections);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("CONNECTIONS");

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


        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Contractor");
        mUserRef1 = FirebaseDatabase.getInstance().getReference().child("Client");
        mUserRef2 = FirebaseDatabase.getInstance().getReference().child("Friends").child(mUser.getUid());

        LoadConnect("");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileImageHeader=view.findViewById(R.id.profileImage_header);
        usernameHeader = view.findViewById(R.id.username_header);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void LoadConnect(String s) {
        Query query = mUserRef2.orderByChild("Client_name").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<ContractorConnectionsHolder>().setQuery(query, ContractorConnectionsHolder.class).build();
        adapter = new FirebaseRecyclerAdapter<ContractorConnectionsHolder, ConnectContractor>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConnectContractor holder, int position, @NonNull ContractorConnectionsHolder model) {
                Picasso.get().load(model.getClient_image()).into(holder.client_image);
                holder.client_name.setText(model.getClient_name());
                holder.client_link.setText(model.getClient_link());
                holder.client_email.setText(model.getClient_email());
                holder.client_phone.setText(model.getClient_phone());
                holder.concon.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                holder.conlayout.setVisibility(View.GONE);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int visibility = (holder.conlayout.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                        TransitionManager.beginDelayedTransition(holder.conlayout, new AutoTransition());
                        holder.conlayout.setVisibility(visibility);

                        if (visibility == View.VISIBLE) {
                            holder.button.setBackgroundResource(R.drawable.up); // Set to "up" drawable when visible
                        } else {
                            holder.button.setBackgroundResource(R.drawable.down); // Set to "down" drawable when gone
                        }
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = model.getClient_uid();
                        Intent intent = new Intent(ContractorConnections.this, ViewProfileOfClient.class);
                        intent.putExtra("uid", uid);
                        startActivityWithAnimation(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ConnectContractor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_connections, parent, false);
                return new ConnectContractor(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
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
                        Picasso.get().load(profileUri).into(profileImageHeader);
                        usernameHeader.setText(username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void SendUserToLogin() {
        Intent intent = new Intent(ContractorConnections.this, LoginFormActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(ContractorConnections.this, Contractor.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(ContractorConnections.this, CcontractorProfile.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(ContractorConnections.this, ContractorConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(ContractorConnections.this, MainActivity.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.addproject) {
            Intent intent = new Intent(ContractorConnections.this, AddProject.class);
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