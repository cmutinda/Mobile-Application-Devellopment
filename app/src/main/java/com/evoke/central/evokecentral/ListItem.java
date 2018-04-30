package com.evoke.central.evokecentral;

/**
 * Created by Castrol on 10/21/2017.
 */

public class ListItem {
    private String head;
    private String desc;
    private String imageUrl;

    public ListItem(String original_name, String head, String desc, String poster_path) {
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
