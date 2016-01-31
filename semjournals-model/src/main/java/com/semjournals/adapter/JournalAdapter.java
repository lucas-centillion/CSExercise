package com.semjournals.adapter;

import com.semjournals.model.Journal;

public class JournalAdapter {
    private String id;
    private AccountAdapter creator;
    private String name;
    private boolean active;

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
}
