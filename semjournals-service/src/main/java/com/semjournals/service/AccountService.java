package com.semjournals.service;

import com.semjournals.adapter.AccountAdapter;
import com.semjournals.data.dao.AccountDAO;
import com.semjournals.data.dao.RoleDAO;
import com.semjournals.encryption.HashUtil;
import com.semjournals.model.Account;
import com.semjournals.model.Role;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountService {

    public List<Account> list() {
        List<Account> accountList = new AccountDAO().list();
        return accountList;
    }

    public List<Account> list(int offset, int limit) {
        List<Account> accountList = new AccountDAO().list(offset, limit);
        return accountList;
    }

    public Account get(String accountId) {
        return new AccountDAO().get(accountId);
    }

    public Account create(AccountAdapter accountAdapter) throws UnsupportedEncodingException {
        checkNotNull(accountAdapter);
        checkNotNull(accountAdapter.getFullname());
        checkNotNull(accountAdapter.getEmail());
        checkNotNull(accountAdapter.getPassword());

        Account createAccount = accountAdapter.fromAdapter(true);

        String[] hashedPassword;
        try {
            hashedPassword = HashUtil.encrypt(accountAdapter.getPassword());
        } catch (UnsupportedEncodingException e) {
            //log
            throw e;
        }

        createAccount.setPassword(hashedPassword[0]);
        createAccount.setSalt(hashedPassword[1]);

        Role role = new RoleDAO().get("name","user");
        createAccount.setRole(role);

        AccountDAO accountDAO = new AccountDAO();
        accountDAO.create(createAccount);
        return accountDAO.get(createAccount.getId());
    }

    public Account update(AccountAdapter accountAdapter) {
        checkNotNull(accountAdapter);
        checkNotNull(accountAdapter.getId());

        AccountDAO accountDAO = new AccountDAO();
        Account currentAccount = accountDAO.get(accountAdapter.getId());

        Account updateAccount = accountAdapter.fromAdapter();
        updateAccount.setPassword(currentAccount.getPassword());
        updateAccount.setSalt(currentAccount.getSalt());

        if (updateAccount.getRole() == null) {
            updateAccount.setRole(currentAccount.getRole());
        }
        if (updateAccount.getFullname() == null) {
            updateAccount.setFullname(currentAccount.getFullname());
        }
        if (updateAccount.getEmail() == null) {
            updateAccount.setEmail(currentAccount.getEmail());
        }
        updateAccount.setActive(currentAccount.isActive());

        accountDAO.update(updateAccount);
        return accountDAO.get(accountAdapter.getId());
    }

    public void delete(String accountId) {
        checkNotNull(accountId);

        Account account = get(accountId);
        checkNotNull(account);
        checkNotNull(account.getId());

        new AccountDAO().delete(account);
    }

}
