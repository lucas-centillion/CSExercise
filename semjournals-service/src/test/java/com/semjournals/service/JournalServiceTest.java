package com.semjournals.service;

import com.semjournals.adapter.JournalAdapter;
import com.semjournals.data.dao.AccountDAO;
import com.semjournals.data.dao.DAO;
import com.semjournals.data.dao.JournalDAO;
import com.semjournals.model.Account;
import com.semjournals.model.Journal;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JournalServiceTest {

    @Test
    public void testList() throws Exception {
        List<Journal> journalList = new JournalService().list();

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(journalList.size() >= 4);
    }

    @Test
    public void testListActive() throws Exception {
        List<Journal> journalList = new JournalService().listActive();

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(journalList.size() >= 2);
        for (Journal journal : journalList) {
            assert(journal.isActive());
        }
    }

    @Test
    public void testGet() throws Exception {
        String id = "a48c772c-34cd-4618-875d-5bdcdc871349";
        Journal journal = new JournalService().get(id);

        validateDefaultJournal(journal);
    }

    @Test
    public void testGet_notFound() throws Exception {
        String id = "does not exist";
        Journal journal = new JournalService().get(id);

        assertNull(journal);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullObject() throws Exception {
        new JournalService().create(null);
    }

    @Test
    public void testCreate() throws Exception {
        JournalAdapter journalAdapter = createJournalAdapter();

        Journal createdJournal = new JournalService().create(journalAdapter);

        assertNotNull(createdJournal);
        assertNotNull(createdJournal.getCreator());
        assertNotNull(createdJournal.getCreator().getId());
        assertEquals(journalAdapter.getCreator().getId(), createdJournal.getCreator().getId());
        assertNotNull(createdJournal.getName());
        assertEquals(journalAdapter.getName(), createdJournal.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_nullObject() throws Exception {
        new JournalService().create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingNotNullData() throws Exception {
        JournalAdapter journal = createJournalAdapter();
        journal.setName(null);

        new JournalService().create(new JournalAdapter(new Journal()));
    }

    @Test
    public void testUpdate() throws Exception {
        String originalName = "Plumbing 101";
        Journal journal = new JournalDAO().get("name",originalName);
        assertNotNull(journal);

        String updatedName = "Update Test";
        JournalAdapter updateJournalAdapter = new JournalAdapter(journal);
        updateJournalAdapter.setName(updatedName);

        Journal updatedJournal = new JournalService().update(updateJournalAdapter);
        assertNotNull(updatedJournal);
        assertNotNull(updatedJournal.getName());
        assertEquals(updatedName, updatedJournal.getName());

        updateJournalAdapter.setName(originalName);

        updatedJournal = new JournalService().update(updateJournalAdapter);
        assertNotNull(updatedJournal);
        assertNotNull(updatedJournal.getName());
        assertEquals(originalName, updatedJournal.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_nullObject() throws Exception {
        new JournalService().update(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_emptyObject() throws Exception {
        new JournalService().update(new JournalAdapter(new Journal()));
    }

    @Test
    public void testDelete() throws Exception {
        JournalAdapter journalAdapter = createJournalAdapter();

        Journal createdJournal = new JournalService().create(journalAdapter);
        assertNotNull(createdJournal);
        assertNotNull(createdJournal.getCreator());
        assertNotNull(createdJournal.getCreator().getId());
        assertNotNull(createdJournal.getId());
        assertNotNull(createdJournal.getName());

        new JournalService().delete(createdJournal.getId());

        createdJournal = new JournalService().get(createdJournal.getId());
        assertNull(createdJournal);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_nullObject() throws Exception {
        new JournalService().delete(null);
    }

    private JournalAdapter createJournalAdapter() {
        String time = String.valueOf(System.currentTimeMillis());

        DAO<Account> daoAccount = new AccountDAO();
        Account creator = daoAccount.get("6270dddf-9945-41eb-baea-3892eca8c063");

        String name = "name_JournalDAOTest_" + time;

        Journal journal = new Journal();
        journal.setCreator(creator);
        journal.setName(name);

        JournalAdapter journalAdapter = new JournalAdapter(journal);

        return journalAdapter;
    }

    private void validateDefaultJournal(Journal journal) {
        // Data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        String creatorId = "6270dddf-9945-41eb-baea-3892eca8c063";
        String creatorName = "Mario";
        String name = "The Mushroom Kingdom History";
        boolean active = true;

        assertNotNull(journal);
        assertNotNull(journal.getCreator());
        assertNotNull(journal.getCreator().getId());
        assertEquals(creatorId, journal.getCreator().getId());
        assertEquals(creatorName, journal.getCreator().getFullname());

        assertNotNull(journal.getId());
        assertNotNull(journal.getName());
        assertEquals(name, journal.getName());
        assertEquals(active, journal.isActive());
    }
}