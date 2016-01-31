package com.semjournals.adapter;

import com.semjournals.model.Journal;

import java.util.Set;

public class JournalAdapter {
    private String id;
    private AccountAdapter creator;
    private String name;
    private boolean active;
    private boolean subscribed;
    private Set<AccountAdapter> subscribers;

    public JournalAdapter() {}

    public JournalAdapter(Journal journal) {
        this.id = journal.getId();
        this.creator = new AccountAdapter(journal.getCreator());
        this.name = journal.getName();
        this.active = journal.isActive();
    }

    public Journal fromAdapter() {
        return fromAdapter(false);
    }
    public Journal fromAdapter(boolean keepCreatedId) {
        Journal journal = new Journal();

        AccountAdapter accountAdapter = this.getCreator();
        if (accountAdapter != null) {
            journal.setCreator(accountAdapter.fromAdapter());
        }

        if (!keepCreatedId) {
            journal.setId(this.getId());
        }

        journal.setName(this.getName());
        journal.setActive(this.isActive());

        return journal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountAdapter getCreator() {
        return creator;
    }

    public void setCreator(AccountAdapter creator) {
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

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Set<AccountAdapter> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<AccountAdapter> subscribers) {
        this.subscribers = subscribers;
    }
}
