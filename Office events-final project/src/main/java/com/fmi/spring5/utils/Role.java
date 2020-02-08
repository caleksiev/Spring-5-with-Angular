package com.fmi.spring5.utils;

public enum Role {
    DEV("DEV"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN");

    String role;

     Role(String role) {
        this.role = role;
    }

    public String getStringRole(){
         return role;
    }
}
