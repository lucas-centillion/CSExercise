package com.semjournals.data.dao;

import com.semjournals.model.Journal;

import static com.google.common.base.Preconditions.checkNotNull;

public class JournalDAO extends GenericDAO<Journal> {
    public JournalDAO() {
        super(Journal.class);
    }

    @Override
    public Journal create(Journal journal) {
        validateJournal(journal);
        return super.create(journal);
    }

    private void validateJournal(Journal journal) {
        checkNotNull(journal);
        checkNotNull(journal.getName());
        checkNotNull(journal.getCreator());
    }

}
