package ro.planner.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import ro.planner.DTO.UserDTO;
import ro.planner.config.UserAuthenticationToken;
import ro.planner.model.Role;
import ro.planner.model.User;
import ro.planner.service.RoleService;
import ro.planner.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class gets the data from the User class from model package and implements operations that will be used
 * later in front-end.
 */

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(final UserService userService,
                          final RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * This method is used to create a new user
     * @param userDTO
     */
    @PostMapping("create-user")
    @ResponseBody
    void createUser(@RequestBody @Valid final UserDTO userDTO) {
        if(userDTO.getId() == null) {
            final ModelMapper modelMapper = new ModelMapper();
            final User user = modelMapper.map(userDTO, User.class);
            final Role role = getRoleService().getById(user.getRole().getId());
            user.setRole(role);
            getUserService().saveOrUpdate(user);
        }
    }

    /**
     * This method is used to update data on user
     * @param userDTO
     */
    @PostMapping
    @ResponseBody
    void saveOrUpdate(@RequestBody @Valid final UserDTO userDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final User user = modelMapper.map(userDTO, User.class);
        final Role role = getRoleService().getById(user.getRole().getId());
        user.setRole(role);
        getUserService().saveOrUpdate(user);
    }

    /**
     * This method implements the login operation for a user from the security part
     * @param userDTO
     * @return authentication token for user
     */
    @PostMapping("/login")
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public UserAuthenticationToken login(@RequestBody UserDTO userDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        final User user = modelMapper.map(userDTO, User.class);
        User loggedUser = getUserService().login(user);

        final String credentials = loggedUser.getUsername() + ":" + loggedUser.getPassword();
        final String authorization = "Basic " + Base64.encodeBase64String(credentials.getBytes());

        final UserAuthenticationToken authenticationToken = new UserAuthenticationToken();

        authenticationToken.setAuthenticationToken(authorization);
        authenticationToken.setUserId(loggedUser.getId());
        authenticationToken.setUserRole(loggedUser.getRole().getType());
        authenticationToken.setPersonId(getUserService().getById(loggedUser.getId()).getPerson().getId());

        return authenticationToken;
    }

    /**
     * This method gets all the users from the data base
     * @return list of users
     */
    @GetMapping
    @ResponseBody
    public List<UserDTO> getAll() {
        final ModelMapper modelMapper = new ModelMapper();
        final List<User> users = getUserService().getAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    /**
     * This method gets the user that has the id = id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    public UserDTO getById(@PathVariable final Long id) {
        final ModelMapper modelMapper = new ModelMapper();
        final User user = getUserService().getById(id);
        return modelMapper.map(user, UserDTO.class);
    }

    public UserService getUserService() {
        return userService;
    }

    public RoleService getRoleService() {
        return roleService;
    }
}
