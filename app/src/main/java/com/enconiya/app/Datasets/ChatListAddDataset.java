package com.enconiya.app.Datasets;

public class ChatListAddDataset {
    String mainkey;
    String secretkey;

    public ChatListAddDataset(String mainkey, String secretkey) {
        this.mainkey = mainkey;
        this.secretkey = secretkey;
    }

    public ChatListAddDataset() {
    }


    public String getMainkey() {
        return mainkey;
    }

    public void setMainkey(String mainkey) {
        this.mainkey = mainkey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }
}
