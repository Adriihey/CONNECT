package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindClientConnection extends RecyclerView.ViewHolder {

    CircleImageView profile;
    TextView clientusername, jtitle, jcompany, dstrct, address, exp, spcl;
    public FindClientConnection(@NonNull View itemView) {
        super(itemView);
        profile = itemView.findViewById(R.id.profile);
        clientusername = itemView.findViewById(R.id.clientusername);
        jtitle = itemView.findViewById(R.id.jtitle);
        jcompany = itemView.findViewById(R.id.jcompany);
        dstrct = itemView.findViewById(R.id.dstrct);
        address = itemView.findViewById(R.id.addcomp);
        exp = itemView.findViewById(R.id.expyears);
        spcl = itemView.findViewById(R.id.job_specialization);
    }
}
