package com.enconiya.app.Datasets;

public class UserDataset {
    String name;
    String email;
    String number;
    String img;
    Object chatlist;

    public UserDataset(String name, String email, String number, String img, Object chatlist) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.img = img;
        this.chatlist = chatlist;
    }

    public UserDataset() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Object getChatlist() {
        return chatlist;
    }

    public void setChatlist(Object chatlist) {
        this.chatlist = chatlist;
    }
}
