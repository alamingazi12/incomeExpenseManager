package com.gktech.incomeexpense.model;

public class MenuItem {

    int image;
    String name;

    public MenuItem(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
