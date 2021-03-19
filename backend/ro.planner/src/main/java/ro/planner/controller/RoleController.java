package ro.planner.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.RoleDTO;
import ro.planner.model.Role;
import ro.planner.service.RoleService;

import javax.validation.Valid;

/**
 * This class gets the data from the Role class from model package and implements operations that will be used
 * later in front-end.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    private RoleService roleService;

    @Autowired
    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * This method creates/updates a role
     * @param roleDTO
     */
    @PostMapping
    @ResponseBody
    void saveOrUpdate(@RequestBody @Valid final RoleDTO roleDTO){
        final ModelMapper modelMapper = new ModelMapper();
        final Role role = modelMapper.map(roleDTO, Role.class);
        getRoleService().saveOrUpdate(role);
    }

    public RoleService getRoleService() {
        return roleService;
    }
}
