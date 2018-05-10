package com.example.yuanyuanlai.drivenoworry.Bean;

public class Tab {

    private int stringId;

    private int imageId;

    public Tab(int stringId, int imageId, Class fragment) {
        this.stringId = stringId;
        this.imageId = imageId;
        this.fragment = fragment;
    }

    private Class fragment;

    public Class getFragment() {
        return fragment;
    }

    public int getStringId() {
        return stringId;
    }

    public int getImageId() {
        return imageId;
    }

}
