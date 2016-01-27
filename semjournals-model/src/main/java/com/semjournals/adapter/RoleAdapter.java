package com.semjournals.adapter;

import com.semjournals.model.Role;

public class RoleAdapter {
    private String id;
    private String name;

    public RoleAdapter() {}

    public RoleAdapter(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    public Role fromAdapter() {
        Role role = new Role();

        role.setId(this.getId());
        role.setName(this.getName());

        return role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
