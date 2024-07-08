package com.example.myapplication.Utills;

public class ContractorUsers {
    private String fullname, budget, building, image, district, location, uid, combinedKey;

    public ContractorUsers() {

    }
    public ContractorUsers(String fullname, String budget, String building, String image, String district, String location, String uid, String combinedKey){
        this.fullname = fullname;
        this.budget = budget;
        this.building = building;
        this.image = image;
        this.district = district;
        this.location = location;
        this.uid = uid;
        this.combinedKey = combinedKey;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCombinedKey() {
        return combinedKey;
    }

    public void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }
}
