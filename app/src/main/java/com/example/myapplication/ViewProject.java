package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewProject extends AppCompatActivity {

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String picture, name, location, bim, type, floor, cfa, description;
    ImageView imageView;
    TextView pcfa, pname, ploc, pbim, ptype, pfloor, pdes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);


        String uid = getIntent().getStringExtra("uid");
        String project_uid = getIntent().getStringExtra("project_uid");

        Toast.makeText(ViewProject.this, ""+uid, Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference()
                .child("Contractor").child(uid)
                .child("Contractor Project").child(project_uid);

        imageView = findViewById(R.id.projectimage);
        pcfa = findViewById(R.id.cfa_project);
        pname = findViewById(R.id.name_project);
        ploc = findViewById(R.id.location_project);
        pbim = findViewById(R.id.bim_project);
        ptype = findViewById(R.id.type_project);
        pfloor = findViewById(R.id.floors_project);
        pdes = findViewById(R.id.description_project);

        LoadProject();
    }

    private void LoadProject() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Object value = childSnapshot.getValue();
                    Log.d("Snapshot", "Key: " + key + ", Value: " + value);
                }
                if (snapshot.exists()){
                    picture = snapshot.child("image").getValue(String.class);
                    name = snapshot.child("project_name").getValue(String.class);
                    location = snapshot.child("project_address").getValue(String.class);
                    bim = snapshot.child("BIM").getValue(String.class);
                    floor = snapshot.child("floor_number").getValue(String.class);
                    type = snapshot.child("type_of_service").getValue(String.class);
                    cfa = snapshot.child("cpa").getValue(String.class);
                    description = snapshot.child("project_details").getValue(String.class);

                    Picasso.get().load(picture).into(imageView);

                    pname.setText(name);
                    pcfa.setText(cfa);
                    ploc.setText(location);
                    pbim.setText(bim);
                    ptype.setText(type);
                    pfloor.setText(floor);
                    pdes.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}