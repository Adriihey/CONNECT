package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileOfClient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FirebaseRecyclerOptions<Projects> options;
    FirebaseRecyclerAdapter<Projects, ProjectHolder> adapter;
    androidx.appcompat.widget.Toolbar toolbar;
    DatabaseReference databaseReference, databaseReference1, reqRef, friendRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    NavigationView navigationView;
    String profileUri, username, cbudget, cbuilding, cdistrict, cemail, clink, clocation, cphone, cwork, profileUri1, username1, email1, fb_link1, phone1;
    CircleImageView profileImageHeader, profilephoto;
    TextView usernameHeader, budget, work, building, disctrict, email, link, loc, phone, clientname;
    Button send, decline;
    LinearLayout linearLayout, linearLayout1, shown, hidden;
    CardView client, contact;
    String state = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_of_client);

        client = findViewById(R.id.clientcard);
        contact = findViewById(R.id.contactcard);
        send = findViewById(R.id.sendrequest);
        decline = findViewById(R.id.declineRequest);
        decline.setVisibility(View.GONE);

        shown = findViewById(R.id.contentshown);
        shown.setVisibility(View.GONE);
        hidden = findViewById(R.id.contenthidden);

        linearLayout = findViewById(R.id.clientlayout);
        linearLayout1 = findViewById(R.id.contactlayout);
        linearLayout1.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        client.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        contact.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int w = (linearLayout.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(linearLayout, new AutoTransition());
                linearLayout.setVisibility(w);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int c = (linearLayout1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(linearLayout1, new AutoTransition());
                linearLayout1.setVisibility(c);
            }

        });

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        String userID = getIntent().getStringExtra("uid");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Client").child(userID);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Contractor");
        reqRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        LoadUser();

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

        profilephoto = findViewById(R.id.profile_image);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        budget = findViewById(R.id.userlink);
        work = findViewById(R.id.scope);
        building = findViewById(R.id.buiding);
        disctrict = findViewById(R.id.district);
        email = findViewById(R.id.email);
        link = findViewById(R.id.link);
        loc = findViewById(R.id.location);
        phone = findViewById(R.id.number);
        clientname = findViewById(R.id.clientname);

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
                        hashMap.put("Client_name", username1);
                        hashMap.put("Client_image", profileUri1);
                        hashMap.put("Client_email", cemail);
                        hashMap.put("Client_link", clink);
                        hashMap.put("Client_phone", cphone);
                        hashMap.put("Client_uid", userID);
                        friendRef.child(firebaseUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    HashMap hashMap1 = new HashMap();
                                    hashMap1.put("status", "connected");
                                    hashMap1.put("Contractor_name", username);
                                    hashMap1.put("Contractor_image", profileUri);
                                    hashMap1.put("Contractor_email", email1);
                                    hashMap1.put("Contractor_link", fb_link1);
                                    hashMap1.put("Contractor_phone", phone1);
                                    hashMap1.put("Contractor_uid", firebaseUser.getUid().toString());
                                    friendRef.child(userID).child(firebaseUser.getUid()).updateChildren(hashMap1);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(ViewProfileOfClient.this, Contractor.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(ViewProfileOfClient.this, CcontractorProfile.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(ViewProfileOfClient.this, ContractorConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(ViewProfileOfClient.this, MainActivity.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.addproject) {
            Intent intent = new Intent(ViewProfileOfClient.this, AddProject.class);
            startActivityWithAnimation(intent);

        }

        return false;
    }
    private void startActivityWithAnimation(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
    }
    private void LoadUser() {
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // Assuming the structure of the databaseReference matches that of databaseReference1
                    profileUri = snapshot.child("image").getValue().toString();
                    username = snapshot.child("fullname").getValue().toString();
                    email1 = snapshot.child("email").getValue().toString();
                    fb_link1 = snapshot.child("link").getValue().toString();
                    phone1 = snapshot.child("phone").getValue().toString();
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
                    cbudget= snapshot.child("budget").getValue().toString();
                    cbuilding = snapshot.child("building").getValue().toString();
                    cdistrict = snapshot.child("district").getValue().toString();
                    cemail = snapshot.child("email").getValue().toString();
                    clink = snapshot.child("fb_link").getValue().toString();
                    clocation = snapshot.child("location").getValue().toString();
                    cphone = snapshot.child("phone").getValue().toString();
                    cwork = snapshot.child("work").getValue().toString();

                    Picasso.get().load(profileUri1).into(profilephoto);

                    clientname.setText(username1);
                    budget.setText("₱"+" "+cbudget);
                    work.setText(cwork);
                    disctrict.setText(cdistrict);
                    email.setText(cemail);
                    phone.setText(cphone);
                    loc.setText(clocation);
                    link.setText(clink);
                    building.setText(cbuilding);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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