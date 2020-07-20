package kg.max.crud.rest;

import kg.max.crud.dto.UserDTO;
import kg.max.crud.model.Role;
import kg.max.crud.model.User;
import kg.max.crud.service.RoleService;
import kg.max.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "/users", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping(value = "/users", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Void> addNewUser(@RequestBody @Validated UserDTO userDTO) {

        User user = convert(userDTO);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userService.insert(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable("userId") long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            userService.update(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/getId", produces = "application/json")
    @ResponseBody
    public Long getLoggedInUserId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return userService.getUserIdByUsername(principal.getName());
    }

    @PutMapping(value = "/users", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Void> editUser(@RequestBody UserDTO userDTO) {
        User user = convert(userDTO);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(userService.getUserPasswordById(user.getId()));
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User convert(UserDTO userDTO) {
        User user = new User(userDTO);
        Set<Role> roles = new HashSet<>();

        for (String role : userDTO.getRoles()) {
            if (role.equals("ROLE_USER")) {
                roles.add(roleService.getRoleById(1));
            }
            if (role.equals("ROLE_ADMIN")) {
                roles.add(roleService.getRoleById(2));
            }
        }
        user.setRoles(roles);
        return user;
    }
}
