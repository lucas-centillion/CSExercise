package com.semjournals.service;

import com.semjournals.data.dao.SubscriptionDAO;
import com.semjournals.model.Account;
import com.semjournals.model.Journal;

import java.util.Set;

public class SubscriptionService {

    public Set<Journal> listSubscriptions(Account account) {
        Set<Journal> subscriptionList = new SubscriptionDAO().listByAccount(account);
        return subscriptionList;
    }

    public Set<Account> listSubscribers(Journal journal) {
        Set<Account> subscriberList = new SubscriptionDAO().listByJournal(journal);
        return subscriberList;
    }

    public void subscribe(Account account, Journal journal) {
        new SubscriptionDAO().create(journal, account);
    }

    public void unsubscribe(Account account, Journal journal) {
        new SubscriptionDAO().delete(journal, account);
    }

}
