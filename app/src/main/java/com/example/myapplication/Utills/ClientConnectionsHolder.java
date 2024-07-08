package com.example.myapplication.Utills;

public class ClientConnectionsHolder {
    private String Contractor_email, Contractor_image, Contractor_link, Contractor_name, Contractor_phone, Contractor_uid;
    public ClientConnectionsHolder(){
    }

    public ClientConnectionsHolder(String contractor_email, String contractor_image, String contractor_link, String contractor_name, String contractor_phone, String contractor_uid) {
        Contractor_email = contractor_email;
        Contractor_image = contractor_image;
        Contractor_link = contractor_link;
        Contractor_name = contractor_name;
        Contractor_phone = contractor_phone;
        Contractor_uid = contractor_uid;
    }

    public String getContractor_email() {
        return Contractor_email;
    }

    public void setContractor_email(String contractor_email) {
        Contractor_email = contractor_email;
    }

    public String getContractor_image() {
        return Contractor_image;
    }

    public void setContractor_image(String contractor_image) {
        Contractor_image = contractor_image;
    }

    public String getContractor_link() {
        return Contractor_link;
    }

    public void setContractor_link(String contractor_link) {
        Contractor_link = contractor_link;
    }

    public String getContractor_name() {
        return Contractor_name;
    }

    public void setContractor_name(String contractor_name) {
        Contractor_name = contractor_name;
    }

    public String getContractor_phone() {
        return Contractor_phone;
    }

    public void setContractor_phone(String contractor_phone) {
        Contractor_phone = contractor_phone;
    }

    public String getContractor_uid() {
        return Contractor_uid;
    }

    public void setContractor_uid(String contractor_uid) {
        Contractor_uid = contractor_uid;
    }
}
