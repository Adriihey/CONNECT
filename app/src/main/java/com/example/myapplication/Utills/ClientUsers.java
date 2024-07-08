package com.example.myapplication.Utills;

public class ClientUsers {
    private String fullname, title, company_name, image, district, experience, location, specialization, uid, districtspecialization, districtspecializationyears, districtyears, specializationyears, expyears;
    public ClientUsers() {

    }


    public ClientUsers(String fullname, String title, String company_name, String image, String district, String experience, String location, String specialization, String uid, String districtspecialization, String specializationyears, String districtyears, String districtspecializationyears, String expyears) {
        this.fullname = fullname;
        this.title = title;
        this.company_name = company_name;
        this.image = image;
        this.district = district;
        this.experience = experience;
        this.location = location;
        this.specialization = specialization;
        this.uid = uid;
        this.districtspecialization = districtspecialization;
        this.districtspecializationyears = districtspecializationyears;
        this.districtyears = districtyears;
        this.specializationyears = specializationyears;
        this.expyears = expyears;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDistrictspecialization() {
        return districtspecialization;
    }

    public void setDistrictspecialization(String districtspecialization) {
        this.districtspecialization = districtspecialization;
    }


    public String getDistrictspecializationyears() {
        return districtspecializationyears;
    }

    public void setDistrictspecializationyears(String districtspecializationyears) {
        this.districtspecializationyears = districtspecializationyears;
    }

    public String getDistrictyears() {
        return districtyears;
    }

    public void setDistrictyears(String districtyears) {
        this.districtyears = districtyears;
    }

    public String getSpecializationyears() {
        return specializationyears;
    }

    public void setSpecializationyears(String specializationyears) {
        this.specializationyears = specializationyears;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExpyears() {
        return expyears;
    }

    public void setExpyears(String expyears) {
        this.expyears = expyears;
    }
}
