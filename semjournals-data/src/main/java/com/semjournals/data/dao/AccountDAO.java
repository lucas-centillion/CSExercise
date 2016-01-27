package com.semjournals.data.dao;

import com.semjournals.model.Account;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountDAO extends GenericDAO<Account> {
    public AccountDAO() {
        super(Account.class);
    }

    @Override
    public Account create(Account account) {
        validateAccount(account);
        return super.create(account);
    }

    private void validateAccount(Account account) {
        checkNotNull(account);
        checkNotNull(account.getRole());
        checkNotNull(account.getFullname());
        checkNotNull(account.getEmail());
        checkNotNull(account.getFullname());
        checkNotNull(account.getPassword());
        checkNotNull(account.getSalt());
    }
}
