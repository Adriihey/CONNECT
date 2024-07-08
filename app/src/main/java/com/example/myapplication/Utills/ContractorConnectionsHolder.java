package com.example.myapplication.Utills;

public class ContractorConnectionsHolder {
    private String Client_email, Client_image, Client_link, Client_name, Client_phone, Client_uid;
    public ContractorConnectionsHolder(){
    }
    public ContractorConnectionsHolder(String client_email, String client_image, String client_link, String client_name, String client_phone, String client_uid) {
        Client_email = client_email;
        Client_image = client_image;
        Client_link = client_link;
        Client_name = client_name;
        Client_phone = client_phone;
        Client_uid = client_uid;
    }

    public String getClient_email() {
        return Client_email;
    }

    public void setClient_email(String client_email) {
        Client_email = client_email;
    }

    public String getClient_image() {
        return Client_image;
    }

    public void setClient_image(String client_image) {
        Client_image = client_image;
    }

    public String getClient_link() {
        return Client_link;
    }

    public void setClient_link(String client_link) {
        Client_link = client_link;
    }

    public String getClient_name() {
        return Client_name;
    }

    public void setClient_name(String client_name) {
        Client_name = client_name;
    }

    public String getClient_phone() {
        return Client_phone;
    }

    public void setClient_phone(String client_phone) {
        Client_phone = client_phone;
    }

    public String getClient_uid() {
        return Client_uid;
    }

    public void setClient_uid(String client_uid) {
        Client_uid = client_uid;
    }
}
