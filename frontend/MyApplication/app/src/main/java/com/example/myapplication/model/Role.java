package com.example.myapplication.model;

/**
 * This class represents the Role model used in front-end representing the RoleDTO from
 * back-end
 */
public class Role extends AbstractModel {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
