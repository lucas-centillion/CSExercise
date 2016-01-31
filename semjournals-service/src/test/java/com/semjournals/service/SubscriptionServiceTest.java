package com.semjournals.service;

import com.semjournals.data.dao.AccountDAO;
import com.semjournals.data.dao.JournalDAO;
import com.semjournals.model.Account;
import com.semjournals.model.Journal;
import org.junit.Test;

import java.util.Set;

public class SubscriptionServiceTest {

    @Test
    public void testListSubscriptions() throws Exception {
        Account account = new AccountDAO().get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        Set<Journal> subscriptionList = new SubscriptionService().listSubscriptions(account);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(subscriptionList.size() >= 2);
    }
    @Test(expected = NullPointerException.class)
    public void testListSubscriptions_nullAccount() throws Exception {
        Account account = null;
        new SubscriptionService().listSubscriptions(account);
    }

    @Test
    public void testListSubscribers() throws Exception {
        Journal journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        Set<Account> subscriptionList = new SubscriptionService().listSubscribers(journal);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(subscriptionList.size() >= 1);
    }

    @Test(expected = NullPointerException.class)
    public void testListSubscribers_nullJournal() throws Exception {
        Journal journal = null;
        new SubscriptionService().listSubscribers(journal);
    }

    @Test
    public void testSubscribe() throws Exception {
        Account account = new AccountDAO().get("fa2f9433-1240-4389-9079-94cab6d2b99e");
        Journal journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");

        assert (!checkHasSubscription(account, journal));

        new SubscriptionService().subscribe(account, journal);

        account = new AccountDAO().get("fa2f9433-1240-4389-9079-94cab6d2b99e");
        journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        assert (checkHasSubscription(account, journal));

        new SubscriptionService().unsubscribe(account, journal);

        account = new AccountDAO().get("fa2f9433-1240-4389-9079-94cab6d2b99e");
        journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        assert (!checkHasSubscription(account, journal));
    }

    @Test
    public void testUnsubscribe() throws Exception {
        Account account = new AccountDAO().get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        Journal journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");

        assert (checkHasSubscription(account, journal));

        new SubscriptionService().unsubscribe(account, journal);

        account = new AccountDAO().get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        assert (!checkHasSubscription(account, journal));

        new SubscriptionService().subscribe(account, journal);

        account = new AccountDAO().get("88e9608e-2217-48e5-9dd3-bd830b04e842");
        journal = new JournalDAO().get("e28b4175-60c4-41af-9341-a26e5310e8a2");
        assert (checkHasSubscription(account, journal));
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