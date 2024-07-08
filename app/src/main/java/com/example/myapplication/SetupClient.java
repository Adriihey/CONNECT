package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupClient extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    String [] item1 = {"Surveying",
            "Plumbing Works",
            "Architectural Works",
            "Construction Works",
            "Mechanical Works",
            "Electrical Works"
    };
    String [] item2 = {"Residential",
            "Commercial"
    };

    String [] item3 = {"First District",
            "Second District",
            "Third District",
            "Fourth District",
            "Fifth District",
            "Sixth District",
            "Seventh District",
            "Eighth District"
    };

    AutoCompleteTextView scope, type, district;
    AppCompatButton confirm, cancel;
    ArrayAdapter<String> scopeadapter, typeadapter, districtadapter;
    EditText fullname, budget, address;
    FirebaseAuth mAuth;
    CircleImageView profileImageView;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_client);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profileImageView = findViewById(R.id.profile_image);
        confirm = findViewById(R.id.v1confirm);
        scope = findViewById(R.id.autoscope);
        scopeadapter = new ArrayAdapter<String>(this, R.layout.list_item, item1);
        scope.setAdapter(scopeadapter);
        fullname = findViewById(R.id.fullname);
        address = findViewById(R.id.clientaddress);
        budget = findViewById(R.id.userlink);
        type = findViewById(R.id.autobuilding);
        typeadapter = new ArrayAdapter<String>(this, R.layout.list_item, item2);
        type.setAdapter(typeadapter);
        cancel = findViewById(R.id.cancelprofile);
        district = findViewById(R.id.autodistrict);
        districtadapter = new ArrayAdapter<String>(this, R.layout.list_item, item3);
        district.setAdapter(districtadapter);
        mAuth = FirebaseAuth.getInstance();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        scope.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });
        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetupClient.this, "Registration cancelled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SetupClient.this, MainActivity.class));
                finish();
            }
        });



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, addressclient, clientbudget, buildingtype, scopework, dstrct;
                name = fullname.getText().toString().trim();
                buildingtype = type.getText().toString().trim();
                scopework = scope.getText().toString().trim();
                addressclient = address.getText().toString().trim();
                dstrct = district.getText().toString().trim();
                String budgetInput = budget.getText().toString().trim();
                clientbudget = formatBudget(budgetInput);

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(clientbudget)|| TextUtils.isEmpty(dstrct) || TextUtils.isEmpty(buildingtype)|| TextUtils.isEmpty(scopework)|| TextUtils.isEmpty(addressclient)||imageUri==null) {
                    Toast.makeText(SetupClient.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                }
                else {
                    String email = getIntent().getStringExtra("email");
                    String password = getIntent().getStringExtra("password");
                    progressDialog.show();
                    LottieAnimationView animationView = new LottieAnimationView(SetupClient.this);
                    animationView.setAnimation(R.raw.pd);
                    animationView.loop(true); // Set loop to true
                    animationView.playAnimation();
                    progressDialog.setContentView(animationView);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            storageReference = FirebaseStorage.getInstance().getReference().child("ClientProfilePics");
                            storageReference.child(user.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                HashMap<String, Object> v1 = new HashMap<>();
                                                v1.put("fullname", name);
                                                v1.put("budget", clientbudget);
                                                v1.put("work", scopework);
                                                v1.put("building", buildingtype);
                                                v1.put("location", addressclient);
                                                v1.put("district", dstrct);
                                                v1.put("combinedKey", dstrct+"_"+buildingtype);
                                                v1.put("image", uri.toString());
                                                v1.put("uid", uid);
                                                v1.put("email", email);
                                                v1.put("category", "Client");
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference reference;
                                                reference = database.getReference("Client");
                                                reference.child(user.getUid()).updateChildren(v1);
                                                progressDialog.dismiss();
                                                startActivity(new Intent(SetupClient.this, SetupClient2.class));
                                                finish();
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    });

                }
            }
        });

    }

    private String formatBudget(String budgetInput) {
        try {
            // Convert input to number
            double amount = Double.parseDouble(budgetInput);
            // Format with commas
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###");
            return formatter.format(amount);
        } catch (NumberFormatException e) {
            // Handle invalid input
            return budgetInput;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed(){}
}