package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

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
import android.widget.ImageButton;
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

public class SetupClient2 extends AppCompatActivity {
    private static final int REQUEST_CODE_1 = 101;
    private static final int REQUEST_CODE_2 = 100;
    AppCompatButton confirm;
    EditText contact, page;
    ImageView imageView1, imageView2;
    FirebaseAuth mAuth;
    Uri imageUri1, imageUri2;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    CardView cardView1, cardView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_client2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageView1 = findViewById(R.id.firstid);
        imageView2 = findViewById(R.id.secondid);
        confirm = findViewById(R.id.v2confirm);
        contact = findViewById(R.id.num);
        page = findViewById(R.id.fblink);
        mAuth = FirebaseAuth.getInstance();
        cardView1 = findViewById(R.id.firstcard);
        cardView2 = findViewById(R.id.secondcard);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_2);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number, pagelink;
                number = contact.getText().toString().trim();
                pagelink = page.getText().toString().trim();


                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(pagelink)||imageUri1==null||imageUri2==null) {
                    Toast.makeText(SetupClient2.this, "Please fill up the form completely", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressDialog.show();
                    LottieAnimationView animationView = new LottieAnimationView(SetupClient2.this);
                    animationView.setAnimation(R.raw.pd);
                    animationView.loop(true); // Set loop to true
                    animationView.playAnimation();
                    progressDialog.setContentView(animationView);
                    FirebaseUser user = mAuth.getCurrentUser();
                    storageReference = FirebaseStorage.getInstance().getReference().child("ClientIDs");
                    StorageReference image1Ref = storageReference.child(user.getUid() + "_ID1.jpg");
                    StorageReference image2Ref = storageReference.child(user.getUid() + "_ID2.jpg");
                    image1Ref.putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                image1Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        image2Ref.putFile(imageUri2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    image2Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri2) {
                                                            HashMap<String, Object> v1 = new HashMap<>();
                                                            v1.put("fb_link", pagelink);
                                                            v1.put("phone", number);
                                                            v1.put("id1", uri.toString());
                                                            v1.put("id2", uri2.toString());

                                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                            DatabaseReference reference;
                                                            reference = database.getReference("Client");
                                                            reference.child(user.getUid()).updateChildren(v1);
                                                            progressDialog.dismiss();
                                                            Toast.makeText(SetupClient2.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(SetupClient2.this, LoginFormActivity.class));
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
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_1) {
                imageUri1 = data.getData();
                if (imageUri1 != null) {
                    cardView1.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                }
            } else if (requestCode == REQUEST_CODE_2) {
                imageUri2 = data.getData();
                if (imageUri2 != null) {
                    cardView2.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                }
            }
        }
    }


    @Override
    public void onBackPressed(){}
}