package edu.csce548.project1.core.model;

public class User {

    public Integer userId;
    public String name;

    public User() {
    }

    public User(Integer id, String name) {
        this.userId = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return userId + ": " + name;
    }
}