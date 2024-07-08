package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Utills.Projects;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileOfContractor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseRecyclerOptions<Projects>options;
    FirebaseRecyclerAdapter<Projects, ProjectHolder>adapter;
    androidx.appcompat.widget.Toolbar toolbar;
    DatabaseReference databaseReference, databaseReference1, mUserRef1, reqRef, friendRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String profileUri, username, jcname, jemail, jexp, jlink, jloc, jphone, jspecial, jtitle, jdistrict, jproj, profileUri1, username1, email, fb_link, phone;
    CircleImageView profileImageHeader, profilephoto;
    TextView usernameHeader, job, experience, comname, comloc, special, number, address, link, contractname, dstrct, proj;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    Button send, decline;
    LinearLayout linearLayout, linearLayout1, shown, hidden;
    CardView work, contact;
    String state = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_of_contractor);
        work = findViewById(R.id.workcard);
        contact = findViewById(R.id.contactcard);
        send = findViewById(R.id.sendrequest);
        decline = findViewById(R.id.declineRequest);
        decline.setVisibility(View.GONE);

        shown = findViewById(R.id.contentshown);
        shown.setVisibility(View.GONE);
        hidden = findViewById(R.id.contenthidden);
        linearLayout = findViewById(R.id.testlayout);
        linearLayout1 = findViewById(R.id.worklayout);
        linearLayout1.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        work.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        contact.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w = (linearLayout1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(linearLayout1, new AutoTransition());
                linearLayout1.setVisibility(w);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int c = (linearLayout.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(linearLayout, new AutoTransition());
                linearLayout.setVisibility(c);
            }

        });

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        recyclerView = findViewById(R.id.project_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        String userID = getIntent().getStringExtra("uid");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Contractor").child(userID);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Client");
        // Modify databaseReference1 to directly reference the "Contractor Project" child
        mUserRef1 = FirebaseDatabase.getInstance().getReference().child("Contractor").child(userID).child("Contractor Project");
        reqRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");

        LoadUser();
        LoadProjects("");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userID);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveConnection(userID);
            }
        });
        CheckUser(userID);

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

    private void RemoveConnection(String userID) {
        if (state.equals("connected")){
            friendRef.child(firebaseUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        friendRef.child(userID).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    state = "none";
                                    send.setText("Send Request");
                                    send.setVisibility(View.VISIBLE);
                                    decline.setVisibility(View.GONE);
                                    shown.setVisibility(View.GONE);
                                    hidden.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            });
        }
        if (state.equals("he_sent_pending")){
            reqRef.child(userID).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    state = "none";
                    decline.setVisibility(View.GONE);
                    send.setText("Send Request");
                    send.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void CheckUser(String userID) {
        friendRef.child(firebaseUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    state = "connected";
                    send.setVisibility(View.GONE);
                    decline.setText("Remove");
                    decline.setVisibility(View.VISIBLE);
                    hidden.setVisibility(View.GONE);
                    shown.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    state = "connected";
                    send.setVisibility(View.GONE);
                    decline.setText("Remove");
                    decline.setVisibility(View.VISIBLE);
                    hidden.setVisibility(View.GONE);
                    shown.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqRef.child(firebaseUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        state = "i_sent_pending";
                        send.setText("Cancel Request");
                        send.setVisibility(View.VISIBLE);
                        decline.setVisibility(View.GONE);
                    }
                    if (snapshot.child("status").getValue().toString().equals("decline")){
                        state = "i_sent_decline";
                        send.setText("Cancel Request");
                        send.setVisibility(View.VISIBLE);
                        decline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqRef.child(userID).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        state = "he_sent_pending";
                        send.setText("Accept Request");
                        send.setVisibility(View.VISIBLE);
                        decline.setText("Decline Request");
                        decline.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (state.equals("none")){
            state = "none";
            send.setText("Send Request");
            send.setVisibility(View.VISIBLE);
            decline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userID) {
        if(state.equals("none")){
            HashMap hashMap = new HashMap();
            hashMap.put("status", "pending");
            reqRef.child(firebaseUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        state = "i_sent_pending";
                        send.setText("Cancel Request");
                        send.setVisibility(View.VISIBLE);
                        decline.setVisibility(View.GONE);
                    }

                }
            });

        }
        if (state.equals("i_sent_pending")||state.equals(getString(R.string.i_sent_decline))){
            reqRef.child(firebaseUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        reqRef.child(userID).child(firebaseUser.getUid()).removeValue();
                        state = "none";
                        send.setText("Send Request");
                        send.setVisibility(View.VISIBLE);
                        decline.setVisibility(View.GONE);
                    }
                }
            });

        }
        if (state.equals("he_sent_pending")){
            reqRef.child(firebaseUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        reqRef.child(userID).child(firebaseUser.getUid()).removeValue();
                        HashMap hashMap = new HashMap();
                        hashMap.put("status", "connected");
                        hashMap.put("Contractor_name", username1);
                        hashMap.put("Contractor_image", profileUri1);
                        hashMap.put("Contractor_email", jemail);
                        hashMap.put("Contractor_link", jlink);
                        hashMap.put("Contractor_phone", jphone);
                        hashMap.put("Contractor_uid", userID);
                        friendRef.child(firebaseUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("status", "connected");
                                    hashMap.put("Client_name", username);
                                    hashMap.put("Client_image", profileUri);
                                    hashMap.put("Client_email", email);
                                    hashMap.put("Client_link", fb_link);
                                    hashMap.put("Client_phone", phone);
                                    hashMap.put("Client_uid", firebaseUser.getUid().toString());
                                    friendRef.child(userID).child(firebaseUser.getUid()).updateChildren(hashMap);
                                    state = "connected";
                                    send.setVisibility(View.GONE);
                                    decline.setText("Remove");
                                    decline.setVisibility(View.VISIBLE);
                                    hidden.setVisibility(View.GONE);
                                    shown.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            });



        }

        if (state.equals("connected")){

        }
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

    private void LoadUser() {
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // Assuming the structure of the databaseReference matches that of databaseReference1
                    profileUri = snapshot.child("image").getValue().toString();
                    username = snapshot.child("fullname").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    fb_link = snapshot.child("fb_link").getValue().toString();
                    phone = snapshot.child("phone").getValue().toString();
                    Picasso.get().load(profileUri).into(profileImageHeader);
                    usernameHeader.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profileUri1 = snapshot.child("image").getValue().toString();
                    username1 = snapshot.child("fullname").getValue().toString();
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

                    Picasso.get().load(profileUri1).into(profilephoto);

                    contractname.setText(username1);
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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(ViewProfileOfContractor.this, Client.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(ViewProfileOfContractor.this, ClientProfile.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(ViewProfileOfContractor.this, ClientConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(ViewProfileOfContractor.this, MainActivity.class);
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