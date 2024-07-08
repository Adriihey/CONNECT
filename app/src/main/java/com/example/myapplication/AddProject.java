package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProject extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    EditText name, projloc, bim, service, floors, cpa, details;
    ImageView imageView;
    FirebaseAuth mAuth;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imageView = findViewById(R.id.projectimage);
        name = findViewById(R.id.nameofproject);
        projloc = findViewById(R.id.locationofproject);
        bim = findViewById(R.id.bim);
        service = findViewById(R.id.servicetype);
        floors = findViewById(R.id.floorsnum);
        cpa = findViewById(R.id.cpa);
        details = findViewById(R.id.projdetails);
        mAuth = FirebaseAuth.getInstance();
        confirm = findViewById(R.id.confirm);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname, paddress, pbim, pservice, pfloors, pcpa, pdetails;
                pname = name.getText().toString().trim();
                pbim = bim.getText().toString().trim();
                paddress = projloc.getText().toString().trim();
                pservice = service.getText().toString().trim();
                pfloors = floors.getText().toString().trim();
                pcpa = cpa.getText().toString().trim();
                pdetails = details.getText().toString().trim();

                if (TextUtils.isEmpty(pname) || TextUtils.isEmpty(pbim) || TextUtils.isEmpty(paddress)|| TextUtils.isEmpty(pservice)|| TextUtils.isEmpty(pfloors)|| TextUtils.isEmpty(pcpa)|| TextUtils.isEmpty(pdetails)||imageUri==null) {
                    Toast.makeText(AddProject.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.show();
                    LottieAnimationView animationView = new LottieAnimationView(AddProject.this);
                    animationView.setAnimation(R.raw.pd);
                    animationView.loop(true); // Set loop to true
                    animationView.playAnimation();
                    progressDialog.setContentView(animationView);
                    FirebaseUser user = mAuth.getCurrentUser();
                    storageReference = FirebaseStorage.getInstance().getReference().child("ContractorProjects").child(user.getUid());
                    String filename = System.currentTimeMillis() + ".jpg";
                    storageReference.child(filename).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storageReference.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String projectUID = FirebaseDatabase.getInstance().getReference().push().getKey();

                                        HashMap<String, Object> v1 = new HashMap<>();
                                        v1.put("project_name", pname);
                                        v1.put("project_address", paddress);
                                        v1.put("type_of_service", pservice);
                                        v1.put("BIM", pbim);
                                        v1.put("floor_number", pfloors);
                                        v1.put("cpa", pcpa);
                                        v1.put("project_details", pdetails);
                                        v1.put("image", uri.toString());
                                        v1.put("project_uid", projectUID);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference contractorReference = database.getReference("Contractor");
                                        DatabaseReference userReference = contractorReference.child(user.getUid());
                                        userReference.child("Contractor Project").push().setValue(v1);
                                        progressDialog.dismiss();
                                        startActivity(new Intent(AddProject.this, CcontractorProfile.class));
                                        finish();
                                    }
                                });
                            }
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
            imageView.setImageURI(imageUri);
        }
    }
}