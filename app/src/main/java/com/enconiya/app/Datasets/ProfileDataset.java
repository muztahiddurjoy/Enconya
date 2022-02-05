package com.enconiya.app.Datasets;

public class ProfileDataset {
    String email;
    String number;
    String name;
    String img;
    Object roles;

    public ProfileDataset(String email, String number, String name, String img, Object roles) {
        this.email = email;
        this.number = number;
        this.name = name;
        this.img = img;
        this.roles = roles;
    }

    public ProfileDataset() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Object getRoles() {
        return roles;
    }

    public void setRoles(Object roles) {
        this.roles = roles;
    }
}
