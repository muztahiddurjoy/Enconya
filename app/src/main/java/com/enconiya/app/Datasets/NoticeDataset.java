package com.enconiya.app.Datasets;

public class NoticeDataset {
    String title;
    String desc;
    String img;
    String link;
    String target;
    String date;

    public NoticeDataset() {
    }

    public NoticeDataset(String title, String desc, String img, String link, String target, String date) {
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.link = link;
        this.target = target;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
