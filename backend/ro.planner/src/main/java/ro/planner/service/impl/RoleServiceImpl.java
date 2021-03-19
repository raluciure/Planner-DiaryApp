package ro.planner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.planner.model.Role;
import ro.planner.repository.RoleRepository;
import ro.planner.service.RoleService;

/**
 * This class creates the logic of the application for the Role model by using the methods from RoleRepository
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * This method creates/updates a role
     * @param role
     */
    @Override
    @Transactional
    public void saveOrUpdate(final Role role) {
        getRoleRepository().save(role);
    }

    /**
     * This method gets the role that has the id = id
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Role getById(final Long id) {
       return getRoleRepository().getById(id);
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }
}
