package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Half;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.Utills.ContractorUsers;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Contractor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String selectedDistrict = "";
    private String selectedBuildingType = "";
    FirebaseRecyclerOptions<ContractorUsers>options;
    FirebaseRecyclerAdapter<ContractorUsers, FindConnections>adapter;
    androidx.appcompat.widget.Toolbar toolbar, toolbar1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef, mUserRef1;
    String profileUri, username;
    CircleImageView profileImageHeader;
    TextView usernameHeader, filter;
    RecyclerView recyclerView;
    CardView linearLayout;
    AppCompatButton first, second, third, fourth, fifth, sixth, seventh, eighth, residential, commercial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth= findViewById(R.id.fifth);
        sixth = findViewById(R.id.sixth);
        seventh = findViewById(R.id.seventh);
        eighth = findViewById(R.id.eighth);
        residential = findViewById(R.id.residential);
        commercial = findViewById(R.id.commercial);

        setButtonClickListeners();

        filter = findViewById(R.id.filter);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView customTitle = new TextView(this);
        customTitle.setText("HOME");

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

        LoadUsers("", "");
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileImageHeader=view.findViewById(R.id.profileImage_header);
        usernameHeader = view.findViewById(R.id.username_header);
        navigationView.setNavigationItemSelectedListener(this);

        linearLayout = findViewById(R.id.filtercard);
        linearLayout.setVisibility(View.GONE);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = linearLayout.getVisibility();
                if (visibility == View.GONE) {
                    // If the linearLayout is not visible, show it and change the text to ">"
                    TransitionManager.beginDelayedTransition(linearLayout, new AutoTransition());
                    linearLayout.setVisibility(View.VISIBLE);
                    filter.setText(">");
                } else {
                    // If the linearLayout is visible, hide it and change the text to "FILTER"
                    TransitionManager.beginDelayedTransition(linearLayout, new AutoTransition());
                    linearLayout.setVisibility(View.GONE);
                    filter.setText("FILTER");
                }
            }
        });
    }

    private void setButtonClickListeners() {
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("First District", first, true);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Second District", second, true);
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Third District", third, true);
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Fourth District", fourth, true);
            }
        });

        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Fifth District", fifth, true);
            }
        });

        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Sixth District", sixth, true);
            }
        });
        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Seventh District", seventh, true);
            }
        });

        eighth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Eighth District", eighth, true);
            }
        });

        residential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Residential", residential, false);
            }
        });
        commercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Commercial", commercial, false);
            }
        });
    }

    private void handleButtonClick(String value, AppCompatButton button, boolean isDistrict) {
        if (!button.isSelected()) {
            // Update the selected value
            if (isDistrict) {
                selectedDistrict = value;
                updateButtonColors(button);
            } else {
                selectedBuildingType = value;
                updateButtonColorsBuilding(button);
            }
        } else {
            // Reset the selected value
            if (isDistrict) {
                selectedDistrict = "";
                updateButtonColors(null);
            } else {
                selectedBuildingType = "";
                updateButtonColorsBuilding(null);
            }
            button.setSelected(false);
        }
        LoadUsers(selectedDistrict, selectedBuildingType);

    }

    private void updateButtonColors(AppCompatButton clickedButton) {
        AppCompatButton[] buttons = {first, second, third, fourth, fifth, sixth, seventh, eighth};
        for (AppCompatButton button : buttons) {
            if (button == clickedButton) {
                button.setSelected(true);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Contractor.this, R.color.blue5));
                button.setTextColor(ContextCompat.getColorStateList(Contractor.this, R.color.white));
            } else {
                button.setSelected(false);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Contractor.this, R.color.white));
                button.setTextColor(ContextCompat.getColorStateList(Contractor.this, R.color.black));
            }
        }
    }

    private void updateButtonColorsBuilding(AppCompatButton clickedButton) {
        AppCompatButton[] buttons = {residential, commercial};
        for (AppCompatButton button : buttons) {
            if (button == clickedButton) {
                button.setSelected(true);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Contractor.this, R.color.blue5));
                button.setTextColor(ContextCompat.getColorStateList(Contractor.this, R.color.white));
            } else {
                button.setSelected(false);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Contractor.this, R.color.white));
                button.setTextColor(ContextCompat.getColorStateList(Contractor.this, R.color.black));
            }
        }
    }

    private void LoadUsers(String district, String buildingType) {
        Query query = mUserRef1.orderByChild("fullname").startAt("").endAt("\uf8ff");

        if (!district.isEmpty() && !buildingType.isEmpty()) {
            // Filter by both district and building type using combined key
            String combinedKey = district + "_" + buildingType;
            query = mUserRef1.orderByChild("combinedKey").equalTo(combinedKey);
        } else if (!district.isEmpty()) {
            // Filter by district only
            query = mUserRef1.orderByChild("district").equalTo(district);
        } else if (!buildingType.isEmpty()) {
            // Filter by building type only
            query = mUserRef1.orderByChild("building").equalTo(buildingType);
        }
        options = new FirebaseRecyclerOptions.Builder<ContractorUsers>().setQuery(query, ContractorUsers.class).build();
        adapter = new FirebaseRecyclerAdapter<ContractorUsers, FindConnections>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindConnections holder, int position, @NonNull ContractorUsers model) {
                Picasso.get().load(model.getImage()).into(holder.circleImageView);
                holder.fullname.setText(model.getFullname());
                holder.budget.setText(model.getBudget());
                holder.buildingtype.setText(model.getBuilding());
                holder.district.setText(model.getDistrict());
                holder.location.setText(model.getLocation());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = model.getUid();
                        Intent intent = new Intent(Contractor.this, ViewProfileOfClient.class);
                        intent.putExtra("uid", uid);
                        startActivityWithAnimation(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FindConnections onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
                return new FindConnections(view);
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
        Intent intent = new Intent(Contractor.this, LoginFormActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(Contractor.this, Contractor.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(Contractor.this, CcontractorProfile.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.addproject) {
            Intent intent = new Intent(Contractor.this, AddProject.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(Contractor.this, ContractorConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(Contractor.this, MainActivity.class);
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