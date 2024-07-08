package com.example.myapplication;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindConnections extends RecyclerView.ViewHolder {
    CircleImageView circleImageView;
    TextView fullname, buildingtype, budget, location, district;
    public FindConnections(@NonNull View itemView) {
        super(itemView);

        circleImageView = itemView.findViewById(R.id.circleImageView);
        fullname = itemView.findViewById(R.id.username);
        buildingtype = itemView.findViewById(R.id.useremail);
        budget = itemView.findViewById(R.id.userlink);
        location = itemView.findViewById(R.id.userphone);
        district = itemView.findViewById(R.id.locdistrict);
    }
}
