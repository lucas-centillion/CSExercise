package com.semjournals.rest.util;

import com.semjournals.data.dao.AccountDAO;
import com.semjournals.model.Account;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserUtil {
    public Account getCurrentAccount() {
        Subject subject = SecurityUtils.getSubject();
        Account account = new AccountDAO().get("email", subject.getPrincipal());
        return account;
    }

    public boolean isCurrentUserAdmin() {
        Subject currentUser = SecurityUtils.getSubject();
        return (currentUser.hasRole("admin"));
    }
}
