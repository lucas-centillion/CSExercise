package com.semjournals.data.dao;

import com.semjournals.model.Account;
import com.semjournals.model.Role;
import org.hibernate.NonUniqueResultException;
import org.hibernate.PropertyValueException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AccountDAOTest {

    @Test
    public void testList() throws Exception {
        DAO<Account> dao = new AccountDAO();
        List<Account> accountList = dao.list();

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(accountList.size() >= 4);
    }

    @Test
    public void testList_withLimit() throws Exception {
        int offset = 0;
        int limit = 3;
        DAO<Account> dao = new AccountDAO();
        List<Account> accountList = dao.list(offset, limit);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(accountList.size() == 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidOffset() throws Exception {
        int offset = -1;
        int limit = 3;

        DAO<Account> dao = new AccountDAO();
        dao.list(offset, limit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidLimit() throws Exception {
        int offset = 1;
        int limit = 0;

        DAO<Account> dao = new AccountDAO();
        dao.list(offset, limit);
    }

    @Test
    public void testGet() throws Exception {
        String id = "6270dddf-9945-41eb-baea-3892eca8c063";
        DAO<Account> dao = new AccountDAO();
        Account account = dao.get(id);

        validateDefaultAccount(account);
    }

    @Test
    public void testGet_notFound() throws Exception {
        String id = "does not exist";
        DAO<Account> dao = new AccountDAO();
        Account account = dao.get(id);

        assertNull(account);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullObject() throws Exception {
        DAO<Account> dao = new AccountDAO();
        dao.get(null);
    }

    @Test
    public void testGet_usingUniqueProperties() throws Exception {
        String email = "mario@gmail.com";
        DAO<Account> dao = new AccountDAO();
        Account account = dao.get("email", email);

        validateDefaultAccount(account);
    }

    @Test(expected = NonUniqueResultException.class)
    public void testGet_usingCommonProperties() throws Exception {
        DAO<Account> dao = new AccountDAO();
        dao.get("active", true);
    }

    @Test
    public void testGet_propertyNotFound() throws Exception {
        String fullname = "does not exist";
        DAO<Account> dao = new AccountDAO();
        Account account = dao.get("fullname", fullname);

        assertNull(account);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullProperty() throws Exception {
        DAO<Account> dao = new AccountDAO();
        dao.get("password", null);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullValue() throws Exception {
        DAO<Account> dao = new AccountDAO();
        dao.get(null, "");
    }

    @Test
    public void testCreate() throws Exception {
        Account account = createAccount(true);
        Role role = account.getRole();

        DAO<Account> dao = new AccountDAO();
        Account savedAccount = dao.create(account);

        assertNotNull(savedAccount.getId());
        assertEquals(account.getRole().getId(), role.getId());
        assertEquals(account.getFullname(), savedAccount.getFullname());
        assertEquals(account.getEmail(), savedAccount.getEmail());
        assertEquals(account.getPassword(), savedAccount.getPassword());
        assertEquals(account.getSalt(), savedAccount.getSalt());
        assertEquals(account.isActive(), savedAccount.isActive());
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_nullObject() throws Exception {
        DAO<Account> dao = new AccountDAO();
        dao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingForeignKey() throws Exception {
        Account account = createAccount(false);
        DAO<Account> dao = new AccountDAO();
        dao.create(account);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingNotNullData() throws Exception {
        Account account = createAccount(true);
        account.setEmail(null);

        DAO<Account> dao = new AccountDAO();
        dao.create(account);
    }

    @Test
    public void testUpdate() throws Exception {
        Account account = createAccount(true);
        String originalName = account.getFullname();

        DAO<Account> dao = new AccountDAO();
        Account savedAccount = dao.create(account);

        account = dao.get(savedAccount.getId());
        assertNotNull(account);
        assertNotNull(account.getFullname());
        assert(account.getFullname().equalsIgnoreCase(originalName));

        String updatedName = "Updated_" + originalName;
        account.setFullname(updatedName);
        dao.update(account);

        assertNotNull(account);
        assertNotNull(account.getFullname());
        assert(account.getFullname().equalsIgnoreCase(updatedName));

        account.setFullname(originalName);
        dao.update(account);

        assertNotNull(account);
        assertNotNull(account.getFullname());
        assert(account.getFullname().equalsIgnoreCase(originalName));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_nullObject() throws Exception {
        DAO<Account> dao = new AccountDAO();
        dao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_emptyObject() throws Exception {
        Account account = new Account();
        account.setId(null);
        DAO<Account> dao = new AccountDAO();
        dao.update(account);
    }

    private Account createAccount(boolean withRole) {
        String time = String.valueOf(System.currentTimeMillis());

        Role role = null;
        if (withRole) {
            DAO<Role> daoRole = new RoleDAO();
            role = daoRole.get("ea9d4ac3-b5f4-4416-9542-ab8e396510d7");
        }

        String fullname = "fullname_AccountDAOTest_" + time;
        String email = "email_AccountDAOTest_" + time;
        String password = "fb16ff3e9e82e0d2eb914c4682fe0ae8l2a77afbdc3101487ab55d27583xb26b446fb29d85358faa91b8de852b5647216ca42ae24b5e210e64a0dae581734ddd";
        String salt = "e375cdb0c88408d77047ce1c762acffb6cd27a34f7fa66da1fdad57d770152e1";
        boolean active = true;

        Account account = new Account(role, fullname, email, password, salt, active);

        return account;
    }

    private void validateDefaultAccount(Account account) {
        // Data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        String roleId = "c582671f-0abe-4165-acb0-851d73954156";
        String roleName = "admin";
        String fullname = "Mario";
        String email = "mario@gmail.com";
        String password = "5a3f8f09e2310f6f81499150ad93d6df06976638f70ebb5334f3a1ff8a800c2c57521859e932544fd5ae3fe6f5b3fffc46bb8f693474c25b6e4ed1a89a2810d0";
        String salt = "e375cdb0c88408d77047ce1c762acffb6cd82a34f7fa66da1fdad57d770152e1";
        boolean active = true;

        assertNotNull(account);
        assertNotNull(account.getRole());
        assertNotNull(account.getRole().getId());
        assertEquals(roleId, account.getRole().getId());
        assertEquals(roleName, account.getRole().getName());

        assertNotNull(account.getId());
        assertNotNull(account.getFullname());
        assertEquals(fullname, account.getFullname());
        assertNotNull(account.getEmail());
        assertEquals(email, account.getEmail());
        assertNotNull(account.getPassword());
        assertEquals(password, account.getPassword());
        assertNotNull(account.getSalt());
        assertEquals(salt, account.getSalt());
        assertEquals(active, account.isActive());
    }
}