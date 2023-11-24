package org.example.payloads;


public class UserDto {
    private int id;
    private String name;
    private String roll;

    public UserDto() {
    }

    public UserDto(int id, String name, String roll) {
        this.id = id;
        this.name = name;
        this.roll = roll;
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
