package com.example.myapplication;

import static android.app.PendingIntent.getActivity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupContractor extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    String [] item1 = {"Surveying",
            "Plumbing Works",
            "Architectural Works",
            "Construction Works",
            "Mechanical Works",
            "Electrical Works"
    };
    String [] item2 = {"First District",
            "Second District",
            "Third District",
            "Fourth District",
            "Fifth District",
            "Sixth District",
            "Seventh District",
            "Eighth District"
    };

    AutoCompleteTextView special, district;
    AppCompatButton confirm, cancel;
    ArrayAdapter<String> specialadapter, districtadapter;
    EditText fullname, jtitle, cname, caddress, project, years;
    FirebaseAuth mAuth;
    CircleImageView profileImageView;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_contractor);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profileImageView = findViewById(R.id.profile_image);
        confirm = findViewById(R.id.v1confirm);
        special = findViewById(R.id.autospecial);
        specialadapter = new ArrayAdapter<String>(this, R.layout.list_item, item1);
        special.setAdapter(specialadapter);
        fullname = findViewById(R.id.fullname);
        cname = findViewById(R.id.companyname);
        caddress = findViewById(R.id.companyaddress);
        jtitle = findViewById(R.id.jobtitle);
        project = findViewById(R.id.projects);
        years = findViewById(R.id.years);
        district = findViewById(R.id.autodistrict);
        districtadapter = new ArrayAdapter<String>(this, R.layout.list_item, item2);
        district.setAdapter(districtadapter);
        cancel = findViewById(R.id.cancelprofile);
        mAuth = FirebaseAuth.getInstance();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        special.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                Toast.makeText(SetupContractor.this, "Registration cancelled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SetupContractor.this, MainActivity.class));
                finish();
            }
        });



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, address, company, job, proj, yrs, spcl, dstrct;
                name = fullname.getText().toString().trim();
                company = cname.getText().toString().trim();
                job = jtitle.getText().toString().trim();
                proj = project.getText().toString().trim();
                yrs = years.getText().toString().trim();
                String expYears = "";
                if (!yrs.isEmpty()) {
                    int yearsValue = Integer.parseInt(yrs);
                    if (yearsValue >= 0 && yearsValue <= 4) {
                        expYears = "one";
                    } else if (yearsValue >= 5 && yearsValue <= 10) {
                        expYears = "two";
                    } else if (yearsValue >= 11 && yearsValue <= 15) {
                        expYears = "three";
                    } else if (yearsValue >= 16 && yearsValue <= 20) {
                        expYears = "four";
                    } else if (yearsValue >= 21 && yearsValue <= 25) {
                        expYears = "five";
                    } else if (yearsValue >= 26 && yearsValue <= 100) {
                        expYears = "six";
                    }
                }
                spcl = special.getText().toString().trim();
                address = caddress.getText().toString().trim();
                dstrct = district.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(company) || TextUtils.isEmpty(job)|| TextUtils.isEmpty(proj)|| TextUtils.isEmpty(spcl)|| TextUtils.isEmpty(yrs)|| TextUtils.isEmpty(dstrct)||imageUri==null) {
                    Toast.makeText(SetupContractor.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                }
                else {
                    String email = getIntent().getStringExtra("email");
                    String password = getIntent().getStringExtra("password");
                    progressDialog.show();
                    LottieAnimationView animationView = new LottieAnimationView(SetupContractor.this);
                    animationView.setAnimation(R.raw.pd);
                    animationView.loop(true); // Set loop to true
                    animationView.playAnimation();
                    progressDialog.setContentView(animationView);
                    String finalExpYears = expYears;
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            storageReference = FirebaseStorage.getInstance().getReference().child("ContractorProfilePics");
                            storageReference.child(user.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        storageReference.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                HashMap<String, Object> v1 = new HashMap<>();
                                                v1.put("fullname", name);
                                                v1.put("title", job);
                                                v1.put("experience", yrs);
                                                v1.put("specialization", spcl);
                                                v1.put("company_name", company);
                                                v1.put("location", address);
                                                v1.put("no_of_projects", proj);
                                                v1.put("district", dstrct);
                                                v1.put("image", uri.toString());
                                                v1.put("districtyears", dstrct+"_"+ finalExpYears);
                                                v1.put("specializationyears", spcl+"_"+ finalExpYears);
                                                v1.put("districtspecialization", dstrct+"_"+spcl);
                                                v1.put("districtspecializationyears", dstrct+"_"+spcl+"_"+ finalExpYears);
                                                v1.put("expyears", finalExpYears);
                                                v1.put("uid", uid);
                                                v1.put("email", email);
                                                v1.put("category", "Contractor");
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference reference;
                                                reference = database.getReference("Contractor");
                                                reference.child(user.getUid()).updateChildren(v1);
                                                progressDialog.dismiss();
                                                startActivity(new Intent(SetupContractor.this, SetupContractor2.class));
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