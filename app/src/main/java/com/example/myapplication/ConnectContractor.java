package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectContractor extends RecyclerView.ViewHolder {
    CircleImageView client_image;
    TextView client_email, client_phone, client_link, client_name;
    LinearLayout conlayout;
    ConstraintLayout concon;
    Button button;
    public ConnectContractor(@NonNull View itemView) {
        super(itemView);
        client_image = itemView.findViewById(R.id.circleImageView);
        client_email = itemView.findViewById(R.id.useremail);
        client_phone = itemView.findViewById(R.id.userphone);
        client_name = itemView.findViewById(R.id.username);
        client_link = itemView.findViewById(R.id.userlink);
        conlayout = itemView.findViewById(R.id.conlayout);
        concon = itemView.findViewById(R.id.concon);
        button = itemView.findViewById(R.id.button);
    }
}
