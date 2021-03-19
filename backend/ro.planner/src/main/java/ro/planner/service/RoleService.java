package ro.planner.service;

import ro.planner.model.Role;

/**
 * This interface contains the methods declared in RoleRepository
 */
public interface RoleService {
    void saveOrUpdate(final Role role);
    Role getById(final Long id);
}
