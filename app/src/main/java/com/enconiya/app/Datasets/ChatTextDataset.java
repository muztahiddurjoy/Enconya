package com.enconiya.app.Datasets;

public class ChatTextDataset {
    String text;
    String name;
    String image;
    String timedate;
    String uid;


    public ChatTextDataset(String text, String name, String image, String timedate, String uid) {
        this.text = text;
        this.name = name;
        this.image = image;
        this.timedate = timedate;
        this.uid = uid;
    }

    public ChatTextDataset() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimedate() {
        return timedate;
    }

    public void setTimedate(String timedate) {
        this.timedate = timedate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
