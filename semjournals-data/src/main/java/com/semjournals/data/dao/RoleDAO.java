package com.semjournals.data.dao;

import com.semjournals.model.Role;

import static com.google.common.base.Preconditions.checkNotNull;

public class RoleDAO extends GenericDAO<Role> {
    public RoleDAO() {
        super(Role.class);
    }

    @Override
    public Role create(Role role) {
        validateRole(role);
        return super.create(role);
    }

    private void validateRole(Role role) {
        checkNotNull(role);
        checkNotNull(role.getName());
    }
}
