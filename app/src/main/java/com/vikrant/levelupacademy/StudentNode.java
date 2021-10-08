package com.vikrant.levelupacademy;

import com.google.firebase.firestore.Exclude;

public class StudentNode {

    String id;
    String name;
    String phone;

    public StudentNode() {
    }

    public StudentNode(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(String id) {
        this.id = id;
    }
}
