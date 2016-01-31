package com.semjournals.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="journal")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Journal extends AbstractPersistentObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Account creator;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active;

    @ManyToMany
    @JoinTable(name = "subscriptions", joinColumns = { @JoinColumn(name =
            "journal_id") }, inverseJoinColumns = { @JoinColumn(name = "account_id") })
    private Set<Account> subscribers = new HashSet<>(0);

    public Journal() {}

    public Journal(Account creator, String name, boolean active) {
        this.creator = creator;
        this.name = name;
        this.active = active;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Account> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Account> subscribers) {
        this.subscribers = subscribers;
    }
}
