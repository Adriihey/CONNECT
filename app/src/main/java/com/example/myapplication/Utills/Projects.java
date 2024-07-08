package com.example.myapplication.Utills;

public class Projects {
    private String image, project_name, project_uid, BIM, cpa, floor_number, project_details, project_address, type_of_service;
    public Projects (){
    }

    public Projects(String image, String project_name, String project_uid, String BIM, String cpa, String floor_number, String project_details, String project_address, String type_of_service) {
        this.image = image;
        this.project_name = project_name;
        this.project_uid = project_uid;
        this.BIM = BIM;
        this.cpa = cpa;
        this.floor_number = floor_number;
        this.project_details = project_details;
        this.project_address = project_address;
        this.type_of_service = type_of_service;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_uid() {
        return project_uid;
    }

    public void setProject_uid(String project_uid) {
        this.project_uid = project_uid;
    }

    public String getBIM() {
        return BIM;
    }

    public void setBIM(String BIM) {
        this.BIM = BIM;
    }

    public String getCpa() {
        return cpa;
    }

    public void setCpa(String cpa) {
        this.cpa = cpa;
    }

    public String getProject_details() {
        return project_details;
    }

    public void setProject_details(String project_details) {
        this.project_details = project_details;
    }

    public String getProject_address() {
        return project_address;
    }

    public void setProject_address(String project_address) {
        this.project_address = project_address;
    }

    public String getType_of_service() {
        return type_of_service;
    }

    public void setType_of_service(String type_of_service) {
        this.type_of_service = type_of_service;
    }

    public String getFloor_number() {
        return floor_number;
    }

    public void setFloor_number(String floor_number) {
        this.floor_number = floor_number;
    }
}
