package com.semjournals.data.dao;

import com.semjournals.model.Role;
import com.semjournals.model.Role;
import org.hibernate.NonUniqueResultException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RoleDAOTest {

    @Test
    public void testCreate() throws Exception {
        Role role = createRole();

        DAO<Role> dao = new RoleDAO();
        Role savedRole = dao.create(role);

        assertNotNull(savedRole.getId());
        assertEquals(role.getId() , savedRole.getId());
        assertEquals(role.getName() , savedRole.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_nullObject() throws Exception {
        DAO<Role> dao = new RoleDAO();
        dao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_missingNotNullData() throws Exception {
        Role role = createRole();
        role.setName(null);

        DAO<Role> dao = new RoleDAO();
        dao.create(role);
    }

    @Test
    public void testList() throws Exception {
        DAO<Role> dao = new RoleDAO();
        List<Role> roleList = dao.list();

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(roleList.size() >= 2);
    }

    @Test
    public void testList_withLimit() throws Exception {
        int offset = 0;
        int limit = 1;
        DAO<Role> dao = new RoleDAO();
        List<Role> roleList = dao.list(offset, limit);

        // Based on data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        assert(roleList.size() == 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidLimit() throws Exception {
        int offset = 1;
        int limit = 0;

        DAO<Role> dao = new RoleDAO();
        dao.list(offset, limit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_invalidOffset() throws Exception {
        int offset = -1;
        int limit = 2;

        DAO<Role> dao = new RoleDAO();
        dao.list(offset, limit);
    }

    @Test
    public void testGet() throws Exception {
        String id = "ea9d4ac3-b5f4-4416-9542-ab8e396510d7";
        DAO<Role> dao = new RoleDAO();
        Role role = dao.get(id);

        validateDefaultRole(role);
    }

    @Test
    public void testGet_notFound() throws Exception {
        String id = "does not exist";
        DAO<Role> dao = new RoleDAO();
        Role role = dao.get(id);

        assertNull(role);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullObject() throws Exception {
        DAO<Role> dao = new RoleDAO();
        dao.get(null);
    }

    @Test
    public void testGet_usingUniqueProperties() throws Exception {
        String name = "user";
        DAO<Role> dao = new RoleDAO();
        Role role = dao.get("name", name);

        validateDefaultRole(role);
    }

    @Test
    public void testGet_propertyNotFound() throws Exception {
        String name = "does not exist";
        DAO<Role> dao = new RoleDAO();
        Role role = dao.get("name", name);

        assertNull(role);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullProperty() throws Exception {
        DAO<Role> dao = new RoleDAO();
        dao.get("name", null);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_nullValue() throws Exception {
        DAO<Role> dao = new RoleDAO();
        dao.get(null, "");
    }

    @Test
    public void testUpdate() throws Exception {
        Role role = createRole();
        String name = role.getName();

        DAO<Role> dao = new RoleDAO();
        Role savedRole = dao.create(role);

        role = dao.get(savedRole.getId());
        assertNotNull(role);
        assertNotNull(role.getName());
        assert(role.getName().equalsIgnoreCase(name));

        name = "Updated_" + name;
        role.setName(name);
        dao.update(role);

        assertNotNull(role);
        assertNotNull(role.getName());
        assert(role.getName().equalsIgnoreCase(name));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_nullObject() throws Exception {
        DAO<Role> dao = new RoleDAO();
        dao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdate_emptyObject() throws Exception {
        Role role = new Role();
        role.setId(null);
        DAO<Role> dao = new RoleDAO();
        dao.update(role);
    }

    @Test
    public void testDelete() throws Exception {
        Role role = createRole();

        DAO<Role> dao = new RoleDAO();
        Role savedRole = dao.create(role);

        role = dao.get(savedRole.getId());
        assertNotNull(role);
        assertNotNull(role.getId());
        assertNotNull(role.getName());

        dao.delete(role);

        role = dao.get(savedRole.getId());
        assertNull(role);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_nullObject() throws Exception {
        DAO<Role> dao = new RoleDAO();
        dao.delete(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDelete_emptyObject() throws Exception {
        Role role = new Role();
        role.setId(null);
        DAO<Role> dao = new RoleDAO();
        dao.delete(role);
    }

    private Role createRole() {
        String time = String.valueOf(System.currentTimeMillis());
        String name = "name_RoleDAOTest_" + time;

        Role role = new Role(name);

        return role;
    }

    private void validateDefaultRole(Role role) {
        // Data inserted automatically by flyway. Inserts located at 'src\main\resources\db\migration\V2__Insert_test_role_user.sql'
        String id = "ea9d4ac3-b5f4-4416-9542-ab8e396510d7";
        String name = "user";

        assertNotNull(role);
        assertNotNull(role.getId());
        assert(role.getId().equalsIgnoreCase(id));
        assert(role.getName().equalsIgnoreCase(name));
    }
}