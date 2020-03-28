package com.gktech.incomeexpense.model;

public class Category {

    String cname;
    int image;

    public String getCode() {
        return code;
    }

    String code;

    public Category(String cname, int image,String code) {
        this.cname = cname;
        this.image = image;
        this.code=code;
    }

    public String getCname() {
        return cname;
    }

    public int getImage() {
        return image;
    }
}
