package com.pmkisanyojana.models;

import java.util.List;

public class YojanaTypeModel {
    public static final int PinnedItem = 0;
    public static final int OtherItem = 1;
    String title;
    private int type;
    /*Pinned Items*/
    private List<YojanaModel> pinnedYojanaList;
    /*Others Items*/
    private List<YojanaModel> yojanaModelList;

    public YojanaTypeModel(int type, List<YojanaModel> pinnedYojanaList) {
        this.type = type;
        this.pinnedYojanaList = pinnedYojanaList;
    }
    /*Pinned Items*/

    public YojanaTypeModel(int type, List<YojanaModel> yojanaModelList, String title) {
        this.type = type;
        this.yojanaModelList = yojanaModelList;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public List<YojanaModel> getPinnedYojanaList() {
        return pinnedYojanaList;
    }

    public List<YojanaModel> getYojanaModelList() {
        return yojanaModelList;
    }

    public String getTitle() {
        return title;
    }
    /*Others Items*/
}
