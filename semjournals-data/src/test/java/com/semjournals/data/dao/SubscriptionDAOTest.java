package com.semjournals.data.dao;

import com.semjournals.model.Account;
import com.semjournals.model.Journal;
import org.junit.Test;

import java.util.Set;

public class SubscriptionDAOTest {
    private String journalId = "a48c772c-34cd-4618-875d-5bdcdc871349";
    private String accountId = "1b94669f-6cf1-4ab5-817e-9ffc7f6ba885";

    @Test
    public void testCreate() throws Exception {
        DAO<Journal> journalDao = new JournalDAO();
        Journal journal = journalDao.get(journalId);

        DAO<Account> accountDao = new AccountDAO();
        Account account = accountDao.get(accountId);

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.create(journal, account);

        journal = journalDao.get(journalId);
        account = accountDao.get(accountId);

        assert(journal.getSubscribers().contains(account));
        assert(account.getSubscriptions().contains(journal));
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_NullJournal() throws Exception {
        Journal journal = null;

        DAO<Account> accountDao = new AccountDAO();
        Account account = accountDao.get(accountId);

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.create(journal, account);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_NullAccount() throws Exception {
        DAO<Journal> journalDao = new JournalDAO();
        Journal journal = journalDao.get(journalId);

        Account account = null;

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.create(journal, account);
    }

    @Test
    public void testDelete() throws Exception {
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();

        DAO<Journal> journalDao = new JournalDAO();
        Journal journal = journalDao.get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        DAO<Account> accountDao = new AccountDAO();
        Account account = accountDao.get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        assert(checkHasSubscriber(journal, account));
        assert(checkHasSubscription(account, journal));

        subscriptionDAO.delete(journal, account);
        journal = journalDao.get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        account = accountDao.get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        assert(!checkHasSubscriber(journal, account));
        assert(!checkHasSubscription(account, journal));

            subscriptionDAO.create(journal, account);
        journal = journalDao.get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        account = accountDao.get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        assert(checkHasSubscriber(journal, account));
        assert(checkHasSubscription(account, journal));
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_NullJournal() throws Exception {
        Journal journal = null;

        DAO<Account> accountDao = new AccountDAO();
        Account account = accountDao.get(accountId);

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.delete(journal, account);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_NullAccount() throws Exception {
        DAO<Journal> journalDao = new JournalDAO();
        Journal journal = journalDao.get(journalId);

        Account account = null;

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.delete(journal, account);
    }

    @Test
    public void testListByJournal() throws Exception {
        DAO<Journal> journalDao = new JournalDAO();
        Journal journal = journalDao.get("e28b4175-60c4-41af-9341-a26e5310e8a2");

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        Set<Account> subscribers = subscriptionDAO.listByJournal(journal);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(subscribers.size() >= 1);
    }

    @Test(expected = NullPointerException.class)
    public void testListByJournal_nullJournal() throws Exception {
        Journal journal = null;

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.listByJournal(journal);
    }

    @Test
    public void testListByAccount() throws Exception {
        DAO<Account> accountDao = new AccountDAO();
        Account account = accountDao.get("88e9608e-2217-48e5-9dd3-bd830b04e842");

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        Set<Journal> subscriptions = subscriptionDAO.listByAccount(account);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(subscriptions.size() >= 2);
    }

    @Test(expected = NullPointerException.class)
    public void testListByAccount_nullJournal() throws Exception {
        Account account = null;

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.listByAccount(account);
    }

    private boolean checkHasSubscription(Account account, Journal journal) {
        final boolean[] hasJournal = {false};
        account.getSubscriptions().forEach((subscription) -> {
            if (subscription.getId().equalsIgnoreCase(journal.getId())) {
                hasJournal[0] = true;
            }
        });
        return hasJournal[0];
    }

    private boolean checkHasSubscriber(Journal journal, Account account) {
        final boolean[] hasAccount = {false};
        journal.getSubscribers().forEach((subscriber) -> {
            if (subscriber.getId().equalsIgnoreCase(account.getId())) {
                hasAccount[0] = true;
            }
        });
        return hasAccount[0];
    }
}