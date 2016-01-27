package com.semjournals.model;

import com.semjournals.util.IdGenerator;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractPersistentObject {
    @Id
    private String id = IdGenerator.createId();

    public String getId() {
        return this.id;
    }
    public void setId(String value) {
        this.id = value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AbstractPersistentObject )) {
            return false;
        }
        AbstractPersistentObject other  = (AbstractPersistentObject ) o;
        // if the id is missing, return false
        if (id == null) return false;
        // equivalence by id
        return id.equals(other.getId());
    }

    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }

    public String toString() {
        return this.getClass().getName()
                + "[id=" + id + "]";
    }
}