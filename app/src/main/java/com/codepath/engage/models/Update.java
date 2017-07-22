package com.codepath.engage.models;

/**
 * Created by awestort on 7/18/17.
 */

public class Update {
    public User user;
    public String description;
    public boolean event;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
