package com.example.myapplication.model;

import java.io.Serializable;

/**
 * This is the abstract class that contains the common attribute for all the classes : id
 */
public abstract class AbstractModel implements Serializable {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
