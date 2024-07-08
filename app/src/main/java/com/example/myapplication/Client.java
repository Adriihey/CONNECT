package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

public class Client extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String selectedDistrict = "";
    private String selectedSpecialization = "";
    private String selectedYears = "";
    FirebaseRecyclerOptions<ClientUsers>options;
    FirebaseRecyclerAdapter<ClientUsers, FindClientConnection>adapter;
    androidx.appcompat.widget.Toolbar toolbar;
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
    AppCompatButton first, second, third, fourth, fifth, sixth, seventh, eighth, residential, commercial, archi, design, mech, elec, one, two, three, four, five, six;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        one = findViewById(R.id.less);
        two = findViewById(R.id.ten);
        three = findViewById(R.id.fifteen);
        four = findViewById(R.id.twenty);
        five = findViewById(R.id.twentyfive);
        six = findViewById(R.id.greater);

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
        archi = findViewById(R.id.archi);
        design = findViewById(R.id.design);
        mech = findViewById(R.id.mech);
        elec = findViewById(R.id.elec);

        setButtonClickListeners();

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
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Client");
        mUserRef1 = FirebaseDatabase.getInstance().getReference().child("Contractor");
        LoadClientUsers("","", "");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileImageHeader=view.findViewById(R.id.profileImage_header);
        usernameHeader = view.findViewById(R.id.username_header);
        navigationView.setNavigationItemSelectedListener(this);

        filter = findViewById(R.id.filter);

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
                handleButtonClick("First District", first, true, false);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Second District", second, true, false);
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Third District", third, true, false);
            }
        });

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Fourth District", fourth, true, false);
            }
        });

        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Fifth District", fifth, true, false);
            }
        });
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Sixth District", sixth, true, false);
            }
        });

        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Seventh District", seventh, true, false);
            }
        });

        eighth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Eighth District", eighth, true, false);
            }
        });

        residential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Surveying", residential, false, false);
            }
        });

        commercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Plumbing Works", commercial, false, false);
            }
        });

        archi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Architectural Works", archi, false, false);
            }
        });

        design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Construction Works", design, false, false);
            }
        });

        mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Mechanical Works", mech, false, false);
            }
        });
        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("Electrical Works", elec, false, false);
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("one", one, false, true);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("two", two, false, true);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("three", three, false, true);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("four", four, false, true);
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("five", five, false, true);
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick("six", six, false, true);
            }
        });

    }

        private void handleButtonClick(String value, AppCompatButton button, boolean isDistrict, boolean isYears) {
        if (!button.isSelected()) {
            // Update the selected value
            if (isDistrict) {
                selectedDistrict = value;
                updateButtonColors(button, isDistrict, isYears);
            } else if (isYears) {
                selectedYears = value;
                updateButtonColors(button, isDistrict, isYears);
            } else {
                selectedSpecialization = value;
                updateButtonColors(button, isDistrict, isYears);
            }
        } else {
            // Reset the selected value
            if (isDistrict) {
                selectedDistrict = "";
                updateButtonColors(null, isDistrict, isYears);
            } else if (isYears) {
                selectedYears = "";
                updateButtonColors(null, isDistrict, isYears);
            } else {
                selectedSpecialization = "";
                updateButtonColors(null, isDistrict, isYears);
            }
            button.setSelected(false);
        }
        LoadClientUsers(selectedDistrict, selectedSpecialization, selectedYears);
    }

    private void updateButtonColors(AppCompatButton clickedButton, boolean isDistrict, boolean isYears) {
        AppCompatButton[] buttons;
        if (isDistrict) {
            buttons = new AppCompatButton[]{first, second, third, fourth, fifth, sixth, seventh, eighth};
        } else if (isYears) {
            buttons = new AppCompatButton[]{one, two, three, four, five, six};
        } else {
            buttons = new AppCompatButton[]{residential, commercial, archi, design, mech, elec};
        }

        for (AppCompatButton button : buttons) {
            if (button == clickedButton) {
                button.setSelected(true);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Client.this, R.color.blue5));
                button.setTextColor(ContextCompat.getColorStateList(Client.this, R.color.white));
            } else {
                button.setSelected(false);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Client.this, R.color.white));
                button.setTextColor(ContextCompat.getColorStateList(Client.this, R.color.black));
            }
        }
    }

    private void updateButtonColorsBuilding(AppCompatButton clickedButton) {
        AppCompatButton[] buttons = {residential, commercial, archi, design, mech, elec};
        for (AppCompatButton button : buttons) {
            if (button == clickedButton) {
                button.setSelected(true);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Client.this, R.color.blue5));
                button.setTextColor(ContextCompat.getColorStateList(Client.this, R.color.white));
            } else {
                button.setSelected(false);
                button.setBackgroundTintList(ContextCompat.getColorStateList(Client.this, R.color.white));
                button.setTextColor(ContextCompat.getColorStateList(Client.this, R.color.black));
            }
        }
    }

    private void LoadClientUsers(String district, String specialization, String yearsRange) {
        Query query = mUserRef1.orderByChild("fullname").startAt("").endAt("\uf8ff");

        if (!district.isEmpty() && !specialization.isEmpty() && !yearsRange.isEmpty()) {

            query = mUserRef1.orderByChild("districtspecializationyears")
                    .equalTo(district + "_" + specialization + "_" + yearsRange);
        } else if (!district.isEmpty() && !specialization.isEmpty()) {
            query = mUserRef1.orderByChild("districtspecialization").equalTo(district + "_" + specialization);
        } else if (!district.isEmpty() && !yearsRange.isEmpty()) {
            query = mUserRef1.orderByChild("districtyears").equalTo(district + "_" + yearsRange);
        } else if (!specialization.isEmpty() && !yearsRange.isEmpty()) {
            query = mUserRef1.orderByChild("specializationyears").equalTo(specialization + "_" + yearsRange);
        } else if (!district.isEmpty()) {
            // Filter by district
            query = mUserRef1.orderByChild("district").equalTo(district);
        } else if (!specialization.isEmpty()) {
            // Filter by specialization
            query = mUserRef1.orderByChild("specialization").equalTo(specialization);
        } else if (!yearsRange.isEmpty()) {
            // Filter by years
            query = mUserRef1.orderByChild("expyears").equalTo(yearsRange);
        }
        options = new FirebaseRecyclerOptions.Builder<ClientUsers>().setQuery(query, ClientUsers.class).build();
        adapter = new FirebaseRecyclerAdapter<ClientUsers, FindClientConnection>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindClientConnection holder, int position, @NonNull ClientUsers model) {
                Picasso.get().load(model.getImage()).into(holder.profile);
                holder.clientusername.setText(model.getFullname());
                holder.jtitle.setText(model.getTitle());
                holder.jcompany.setText(model.getCompany_name());
                holder.dstrct.setText(model.getDistrict());
                holder.address.setText(model.getLocation());
                holder.exp.setText(model.getExperience());
                holder.spcl.setText(model.getSpecialization());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uid = model.getUid();
                        Intent intent = new Intent(Client.this, ViewProfileOfContractor.class);
                        intent.putExtra("uid", uid);
                        startActivityWithAnimation(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FindClientConnection onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users_another, parent, false);
                return new FindClientConnection(view);
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
        Intent intent = new Intent(Client.this, LoginFormActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(Client.this, Client.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(Client.this, ClientProfile.class);
            startActivityWithAnimation(intent);
        } else if (itemId == R.id.confirmed) {
            Intent intent = new Intent(Client.this, ClientConnections.class);
            startActivityWithAnimation(intent);

        } else if (itemId == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(Client.this, MainActivity.class);
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