package org.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;


public class UserEntity {

    private int id;
    private String name;
    private String roll;

    public UserEntity(int id, String name, String roll) {
        this.id = id;
        this.name = name;
        this.roll = roll;
    }

    public UserEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
