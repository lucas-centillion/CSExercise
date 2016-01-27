package com.semjournals.model;

import javax.persistence.*;

@Entity
@Table( name = "role" )
@AttributeOverride(name = "id", column = @Column(name = "id"))
@NamedNativeQuery(name = "getRoleByName", query = "select * from role where name = :roleName", resultClass=Role.class)
public class Role extends AbstractPersistentObject {
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
