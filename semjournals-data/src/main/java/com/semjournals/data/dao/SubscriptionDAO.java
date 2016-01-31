package com.semjournals.data.dao;

import com.semjournals.model.Account;
import com.semjournals.model.Journal;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class SubscriptionDAO {
    public void create(Journal journal, Account account) {
        validateJournal(journal);
        validateAccount(account);

        account = new AccountDAO().get(account.getId());
        journal = new JournalDAO().get(journal.getId());
        journal.getSubscribers().add(account);
        new JournalDAO().update(journal);
    }

    public void delete(Journal journal, Account account) {
        validateJournal(journal);
        validateAccount(account);

        account = new AccountDAO().get(account.getId());
        journal = new JournalDAO().get(journal.getId());
        journal.getSubscribers().remove(account);
        new JournalDAO().update(journal);
    }

    public Set<Journal> listByAccount(Account account) {
        validateAccount(account);

        account = new AccountDAO().get(account.getId());
        return account.getSubscriptions();
    }

    public Set<Account> listByJournal(Journal journal) {
        validateJournal(journal);

        journal = new JournalDAO().get(journal.getId());
        return journal.getSubscribers();
    }

    private void validateJournal(Journal journal) {
        checkNotNull(journal);
        checkNotNull(journal.getId());
    }

    private void validateAccount(Account account) {
        checkNotNull(account);
        checkNotNull(account.getId());
    }

}
