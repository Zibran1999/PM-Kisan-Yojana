package com.pmkisanyojana.models;

public class YojanaModel {

    int yojnaImage;
    String yojnaName;
    String yojnaId;

    public YojanaModel(int yojnaImage, String yojnaName, String yojnaId) {
        this.yojnaImage = yojnaImage;
        this.yojnaName = yojnaName;
        this.yojnaId = yojnaId;
    }

    public int getYojnaImage() {
        return yojnaImage;
    }

    public String getYojnaName() {
        return yojnaName;
    }

    public String getYojnaId() {
        return yojnaId;
    }
}
