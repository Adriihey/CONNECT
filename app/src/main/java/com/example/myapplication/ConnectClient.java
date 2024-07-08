package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectClient extends RecyclerView.ViewHolder {
    CircleImageView contractor_image;
    TextView contractor_email, contractor_phone, contractor_link, contractor_name;
    LinearLayout conlayout;
    ConstraintLayout concon;
    Button button;
    public ConnectClient(@NonNull View itemView) {
        super(itemView);
        contractor_image = itemView.findViewById(R.id.circleImageView);
        contractor_email = itemView.findViewById(R.id.useremail);
        contractor_phone = itemView.findViewById(R.id.userphone);
        contractor_name = itemView.findViewById(R.id.username);
        contractor_link = itemView.findViewById(R.id.userlink);
        conlayout = itemView.findViewById(R.id.conlayout);
        concon = itemView.findViewById(R.id.concon);
        button = itemView.findViewById(R.id.button);
    }
}
