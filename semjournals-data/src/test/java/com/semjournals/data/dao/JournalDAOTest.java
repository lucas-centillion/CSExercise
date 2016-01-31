package com.semjournals.data.dao;

import com.semjournals.model.Account;
import com.semjournals.model.Journal;
import org.hibernate.NonUniqueResultException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JournalDAOTest {

    @Test
    public void testList() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        List<Journal> journalList = dao.list();

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(journalList.size() >= 4);
    }

    @Test
    public void testList_withLimit() throws Exception {
        int offset = 0;
        int limit = 3;
        DAO<Journal> dao = new JournalDAO();
        List<Journal> journalList = dao.list(offset, limit);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(journalList.size() == 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidOffset() throws Exception {
        int offset = -1;
        int limit = 3;

        DAO<Journal> dao = new JournalDAO();
        dao.list(offset, limit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidLimit() throws Exception {
        int offset = 1;
        int limit = 0;

        DAO<Journal> dao = new JournalDAO();
        dao.list(offset, limit);
    }

    @Test
    public void testGet() throws Exception {
        String id = "a48c772c-34cd-4618-875d-5bdcdc871349";
        DAO<Journal> dao = new JournalDAO();
        Journal journal = dao.get(id);

        validateDefaultJournal(journal);
    }

    @Test
    public void testGet_notFound() throws Exception {
        String id = "does not exist";
        DAO<Journal> dao = new JournalDAO();
        Journal journal = dao.get(id);

        assertNull(journal);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullObject() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        dao.get(null);
    }

    @Test
    public void testGet_usingUniqueProperties() throws Exception {
        String name = "The Mushroom Kingdom History";
        DAO<Journal> dao = new JournalDAO();
        Journal journal = dao.get("name", name);

        validateDefaultJournal(journal);
    }

    @Test(expected = NonUniqueResultException.class)
    public void testGet_usingCommonProperties() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        dao.get("active", true);
    }

    @Test
    public void testGet_propertyNotFound() throws Exception {
        String fullname = "does not exist";
        DAO<Journal> dao = new JournalDAO();
        Journal journal = dao.get("name", fullname);

        assertNull(journal);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullProperty() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        dao.get("name", null);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullValue() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        dao.get(null, "");
    }

    @Test
    public void testCreate() throws Exception {
        Journal journal = createJournal(true);

        DAO<Journal> dao = new JournalDAO();
        Journal savedJournal = dao.create(journal);

        assertNotNull(savedJournal.getId());
        assertEquals(journal.getCreator().getId(), savedJournal.getCreator().getId());
        assertEquals(journal.getName(), savedJournal.getName());
        assertEquals(journal.isActive(), savedJournal.isActive());
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_nullObject() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        dao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingForeignKey() throws Exception {
        Journal journal = createJournal(false);
        DAO<Journal> dao = new JournalDAO();
        dao.create(journal);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingNotNullData() throws Exception {
        Journal journal = createJournal(true);
        journal.setName(null);

        DAO<Journal> dao = new JournalDAO();
        dao.create(journal);
    }

    @Test
    public void testUpdate() throws Exception {
        Journal journal = createJournal(true);
        String originalName = journal.getName();

        DAO<Journal> dao = new JournalDAO();
        Journal savedJournal = dao.create(journal);

        journal = dao.get(savedJournal.getId());
        assertNotNull(journal);
        assertNotNull(journal.getName());
        assert(journal.getName().equalsIgnoreCase(originalName));

        String updatedName = "Updated_" + originalName;
        journal.setName(updatedName);
        dao.update(journal);

        assertNotNull(journal);
        assertNotNull(journal.getName());
        assert(journal.getName().equalsIgnoreCase(updatedName));

        journal.setName(originalName);
        dao.update(journal);

        assertNotNull(journal);
        assertNotNull(journal.getName());
        assert(journal.getName().equalsIgnoreCase(originalName));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_nullObject() throws Exception {
        DAO<Journal> dao = new JournalDAO();
        dao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_emptyObject() throws Exception {
        Journal journal = new Journal();
        journal.setId(null);
        DAO<Journal> dao = new JournalDAO();
        dao.update(journal);
    }

    private Journal createJournal(boolean withCreator) {
        String time = String.valueOf(System.currentTimeMillis());

        Account account = null;
        if (withCreator) {
            DAO<Account> daoAccount = new AccountDAO();
            account = daoAccount.get("6270dddf-9945-41eb-baea-3892eca8c063");
        }

        String name = "name_JournalDAOTest_" + time;
        boolean active = true;

        Journal journal = new Journal(account, name, active);

        return journal;
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