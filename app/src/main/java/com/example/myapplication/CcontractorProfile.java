package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.Utills.Projects;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class CcontractorProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseRecyclerOptions<Projects>options;
    FirebaseRecyclerAdapter<Projects, ProjectHolder>adapter;
    androidx.appcompat.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef, mUserRef1;
    String profileUri, username, jcname, jemail, jexp, jlink, jloc, jphone, jspecial, jtitle, jdistrict, jproj;
    CircleImageView profileImageHeader, profilephoto;
    TextView usernameHeader, job, experience, comname, comloc, special, number, address, link, contractname, dstrct, proj;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccontractor_profile);

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

        recyclerView = findViewById(R.id.project_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference("Contractor");
        mUserRef1 = mUserRef.child(mUser.getUid()).child("Contractor Project");

        LoadProjects("");
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        profilephoto = findViewById(R.id.profile_image);
        experience = findViewById(R.id.years);
        comname= findViewById(R.id.companyname);
        comloc= findViewById(R.id.companylocation);
        special= findViewById(R.id.specialization);
        number= findViewById(R.id.number);
        address= findViewById(R.id.email);
        job= findViewById(R.id.title);
        proj = findViewById(R.id.projects);
        link = findViewById(R.id.link);
        contractname = findViewById(R.id.contractorname);
        dstrct = findViewById(R.id.companydistrict);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileImageHeader=view.findViewById(R.id.profileImage_header);
        usernameHeader = view.findViewById(R.id.username_header);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void LoadProjects(String s) {
        Query query = mUserRef1.orderByChild("project_name").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Projects>().setQuery(query, Projects.class).build();
        adapter = new FirebaseRecyclerAdapter<Projects, ProjectHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProjectHolder holder, int position, @NonNull Projects model) {
                Picasso.get().load(model.getImage()).into(holder.projectpicture);
                holder.projectname.setText(model.getProject_name());
                holder.location_project.setText(model.getProject_address());
                holder.bim_project.setText(model.getBIM());
                holder.type_project.setText(model.getType_of_service());
                holder.floors_project.setText(model.getFloor_number());
                holder.cfa_project.setText(model.getCpa());
                holder.description_project.setText(model.getProject_details());
                holder.projectcons.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                holder.projectlay.setVisibility(View.GONE);
                holder.projectcons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int o = (holder.projectlay.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                        TransitionManager.beginDelayedTransition(holder.projectlay, new AutoTransition());
                        holder.projectlay.setVisibility(o);
                    }
                });
            }

            @NonNull
            @Override
            public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list, parent,false);
                return new ProjectHolder(view);
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
                        jcname = snapshot.child("company_name").getValue().toString();
                        jemail = snapshot.child("email").getValue().toString();
                        jexp = snapshot.child("experience").getValue().toString();
                        jlink = snapshot.child("link").getValue().toString();
                        jloc = snapshot.child("location").getValue().toString();
                        jphone = snapshot.child("phone").getValue().toString();
                        jspecial = snapshot.child("specialization").getValue().toString();
                        jtitle = snapshot.child("title").getValue().toString();
                        jdistrict = snapshot.child("district").getValue().toString();
                        jproj = snapshot.child("no_of_projects").getValue().toString();
                        Picasso.get().load(profileUri).into(profileImageHeader);
                        Picasso.get().load(profileUri).into(profilephoto);
                        usernameHeader.setText(username);
                        contractname.setText(username);
                        experience.setText(jexp+" "+"years");
                        comname.setText(jcname);
                        comloc.setText(jloc);
                        special.setText(jspecial);
                        number.setText(jphone);
                        address.setText(jemail);
                        job.setText(jtitle);
                        link.setText(jlink);
                        dstrct.setText(jdistrict);
                        proj.setText(jproj);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void SendUserToLogin() {
        Intent intent = new Intent(CcontractorProfile.this, LoginFormActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(CcontractorProfile.this, Contractor.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(CcontractorProfile.this, CcontractorProfile.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.addproject) {
            Intent intent = new Intent(CcontractorProfile.this, AddProject.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(CcontractorProfile.this, ContractorConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(CcontractorProfile.this, MainActivity.class);
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