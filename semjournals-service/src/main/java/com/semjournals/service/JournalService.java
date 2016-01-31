package com.semjournals.service;

import com.semjournals.adapter.JournalAdapter;
import com.semjournals.data.dao.AccountDAO;
import com.semjournals.data.dao.JournalDAO;
import com.semjournals.data.dao.RoleDAO;
import com.semjournals.encryption.HashUtil;
import com.semjournals.model.Account;
import com.semjournals.model.Journal;
import com.semjournals.model.Role;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class JournalService {

    public List<Journal> list() {
        List<Journal> journalList = new JournalDAO().list();
        return journalList;
    }
    public List<Journal> listActive() {
        List<Journal> journalList = new JournalDAO().listActive();
        return journalList;
    }

    public Journal get(String journalId) {
        return new JournalDAO().get(journalId);
    }
    public Journal getByName(String name) {
        return new JournalDAO().get("name", name);
    }

    public Journal create(JournalAdapter journalAdapter) throws IllegalArgumentException {
        checkNotNull(journalAdapter);
        checkNotNull(journalAdapter.getCreator());
        checkNotNull(journalAdapter.getCreator().getId());
        checkNotNull(journalAdapter.getName());

        if (getByName(journalAdapter.getName()) != null) {
            throw new IllegalArgumentException("Journal name '" + journalAdapter.getName() + "' already exists");
        }

        Journal createJournal = journalAdapter.fromAdapter(true);

        Account account = new AccountDAO().get("id", journalAdapter.getCreator().getId());
        createJournal.setCreator(account);

        JournalDAO journalDAO = new JournalDAO();
        journalDAO.create(createJournal);
        return journalDAO.get(createJournal.getId());
    }

    public Journal update(JournalAdapter journalAdapter) throws IllegalArgumentException {
        checkNotNull(journalAdapter);
        checkNotNull(journalAdapter.getId());

        JournalDAO journalDAO = new JournalDAO();
        Journal currentJournal = journalDAO.get(journalAdapter.getId());

        if (!currentJournal.getName().equalsIgnoreCase(journalAdapter.getName())) {
            // name has changed, check on DB if there is a conflict and, if not, update the filename
            boolean hasConflict = journalDAO.get("name", journalAdapter.getName()) != null;

            if (hasConflict) {
                throw new IllegalArgumentException("Journal name '" + journalAdapter.getName() + "' already exists");
            }

            updateFileName(currentJournal.getName(), journalAdapter.getName());
        }

        Journal updateJournal = journalAdapter.fromAdapter();

        if (updateJournal.getCreator() == null) {
            updateJournal.setCreator(currentJournal.getCreator());
        }
        if (updateJournal.getName() == null) {
            updateJournal.setName(currentJournal.getName());
        }
        updateJournal.setActive(currentJournal.isActive());

        journalDAO.update(updateJournal);
        return journalDAO.get(journalAdapter.getId());
    }

    public void delete(String journalId) {
        checkNotNull(journalId);

        Journal journal = get(journalId);
        checkNotNull(journal);
        checkNotNull(journal.getId());

        new JournalDAO().delete(journal);
        deleteFileName(journal.getName());
    }

    private void updateFileName(String currentName, String newName) {
        File file = new File("journals/" + currentName);
        file.renameTo(new File("journals/" + newName));
    }

    private void deleteFileName(String name) {
        new File("journals/" + name).delete();

    }

}
