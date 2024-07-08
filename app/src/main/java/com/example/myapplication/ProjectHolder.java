package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectHolder extends RecyclerView.ViewHolder {
    ImageView projectpicture;
    TextView projectname, location_project, bim_project, type_project, floors_project, cfa_project, description_project;
    ConstraintLayout projectcons;
    LinearLayout projectlay;


    public ProjectHolder(@NonNull View itemView) {
        super(itemView);
        projectpicture = itemView.findViewById(R.id.projectpicture);
        projectname = itemView.findViewById(R.id.projectname);
        location_project = itemView.findViewById(R.id.location_project);
        bim_project = itemView.findViewById(R.id.bim_project);
        type_project = itemView.findViewById(R.id.type_project);
        floors_project = itemView.findViewById(R.id.floors_project);
        cfa_project = itemView.findViewById(R.id.cfa_project);
        description_project = itemView.findViewById(R.id.description_project);
        projectcons = itemView.findViewById(R.id.projectconstraint);
        projectlay = itemView.findViewById(R.id.projectlayout);

    }
}
