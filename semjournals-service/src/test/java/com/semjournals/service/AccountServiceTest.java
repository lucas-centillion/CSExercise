package com.semjournals.service;

import com.semjournals.adapter.AccountAdapter;
import com.semjournals.adapter.RoleAdapter;
import com.semjournals.data.dao.AccountDAO;
import com.semjournals.data.dao.DAO;
import com.semjournals.data.dao.RoleDAO;
import com.semjournals.model.Account;
import com.semjournals.model.Role;
import org.hibernate.NonUniqueResultException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AccountServiceTest {

    @Test
    public void testList() throws Exception {
        List<Account> accountList = new AccountService().list();

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(accountList.size() >= 4);
    }

    @Test
    public void testList_withOffsetLimit() throws Exception {
        int offset = 1;
        int maxSize = 2;
        List<Account> accountList = new AccountService().list(offset, maxSize);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(accountList.size() == 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidOffset() throws Exception {
        int offset = -1;
        int maxSize = 2;
        new AccountService().list(offset, maxSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidLimit() throws Exception {
        int offset = 1;
        int maxSize = 0;
        new AccountService().list(offset, maxSize);
    }

    @Test
    public void testGet() throws Exception {
        String id = "6270dddf-9945-41eb-baea-3892eca8c063";
        Account account = new AccountService().get(id);

        validateDefaultAccount(account);
    }

    @Test
    public void testGet_notFound() throws Exception {
        String id = "does not exist";
        Account account = new AccountService().get(id);

        assertNull(account);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullObject() throws Exception {
        new AccountService().create(null);
    }

    @Test
    public void testCreate() throws Exception {
        AccountAdapter accountAdapter = createAccountAdapter();

        Account createdAccount = new AccountService().create(accountAdapter);

        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getFullname());
        assertEquals(accountAdapter.getFullname(), createdAccount.getFullname());
        assertNotNull(createdAccount.getEmail());
        assertEquals(accountAdapter.getEmail(), createdAccount.getEmail());
        assertNotNull(createdAccount.getRole());
        assertNotNull(createdAccount.getRole().getId());
        assertEquals(accountAdapter.getRole().getId(), createdAccount.getRole().getId());
        assertNotNull(createdAccount.getPassword());
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_nullObject() throws Exception {
        new AccountService().create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingNotNullData() throws Exception {
        AccountAdapter account = createAccountAdapter();
        account.setEmail(null);

        new AccountService().create(new AccountAdapter(new Account()));
    }

    @Test
    public void testUpdate() throws Exception {
        String originalName = "Luigi";
        Account account = new AccountDAO().get("fullname",originalName);
        assertNotNull(account);

        String updatedFullname = "Update Test";
        AccountAdapter updateAccountAdapter = new AccountAdapter(account);
        updateAccountAdapter.setFullname(updatedFullname);

        Account updatedAccount = new AccountService().update(updateAccountAdapter);
        assertNotNull(updatedAccount);
        assertNotNull(updatedAccount.getEmail());
        assertEquals(updatedFullname, updatedAccount.getFullname());

        updateAccountAdapter.setFullname(originalName);

        updatedAccount = new AccountService().update(updateAccountAdapter);
        assertNotNull(updatedAccount);
        assertNotNull(updatedAccount.getEmail());
        assertEquals(originalName, updatedAccount.getFullname());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_nullObject() throws Exception {
        new AccountService().update(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_emptyObject() throws Exception {
        new AccountService().update(new AccountAdapter(new Account()));
    }

    @Test
    public void testDelete() throws Exception {
        AccountAdapter accountAdapter = createAccountAdapter();

        Account createdAccount = new AccountService().create(accountAdapter);
        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getRole());
        assertNotNull(createdAccount.getRole().getId());
        assertNotNull(createdAccount.getId());
        assertNotNull(createdAccount.getFullname());
        assertNotNull(createdAccount.getEmail());
        assertNotNull(createdAccount.getPassword());

        new AccountService().delete(createdAccount.getId());

        createdAccount = new AccountService().get(createdAccount.getId());
        assertNull(createdAccount);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_nullObject() throws Exception {
        new AccountService().delete(null);
    }

    private AccountAdapter createAccountAdapter() {
        String time = String.valueOf(System.currentTimeMillis());

        DAO<Role> daoRole = new RoleDAO();
        Role role = daoRole.get("ea9d4ac3-b5f4-4416-9542-ab8e396510d7");

        String fullname = "fullname_AccountDAOTest_" + time;
        String email = "email_AccountDAOTest_" + time;
        String password = "pwd_AccountDAOTest_" + time;

        Account account = new Account();
        account.setFullname(fullname);
        account.setEmail(email);
        account.setRole(role);

        AccountAdapter accountAdapter = new AccountAdapter(account);
        accountAdapter.setPassword(password);

        return accountAdapter;
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