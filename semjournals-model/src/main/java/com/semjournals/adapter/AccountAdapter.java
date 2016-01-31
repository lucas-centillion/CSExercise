package com.semjournals.adapter;

import com.semjournals.model.Account;

import java.util.Set;

public class AccountAdapter {
    private String id;
    private RoleAdapter role;
    private String fullname;
    private String email;
    private String password;
    private boolean active;
    private Set<JournalAdapter> subscriptions;

    public AccountAdapter() {}

    public AccountAdapter(Account account) {
        this.id = account.getId();
        this.role = new RoleAdapter(account.getRole());
        this.fullname = account.getFullname();
        this.email = account.getEmail();
        this.active = account.isActive();
    }

    public Account fromAdapter() {
        return fromAdapter(false);
    }
    public Account fromAdapter(boolean keepCreatedId) {
        Account account = new Account();

        RoleAdapter roleAdapter = this.getRole();
        if (roleAdapter != null) {
            account.setRole(roleAdapter.fromAdapter());
        }

        if (!keepCreatedId) {
            account.setId(this.getId());
        }

        account.setFullname(this.getFullname());
        account.setEmail(this.getEmail());
        account.setActive(this.isActive());

        return account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleAdapter getRole() {
        return role;
    }

    public void setRole(RoleAdapter role) {
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<JournalAdapter> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<JournalAdapter> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
