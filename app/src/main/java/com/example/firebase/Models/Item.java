package com.example.firebase.Models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Item {
    //Attributtes
    private String name;

    private int quantity;

    private String key;

    public Item(String name, int quantity, String key) {
        this.name = name;
        this.quantity = quantity;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
