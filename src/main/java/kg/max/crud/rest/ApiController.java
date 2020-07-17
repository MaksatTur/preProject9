package kg.max.crud.rest;

import kg.max.crud.dto.UserDTO;
import kg.max.crud.model.Role;
import kg.max.crud.model.User;
import kg.max.crud.service.RoleService;
import kg.max.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/all", produces = "application/json")
    @ResponseBody
    public ServerResponse getAll() {
        List<User> userList = userService.findAll();
        return new ServerResponse("success", userList);
    }

    @PostMapping(value = "/add", produces = "application/json")
    @ResponseBody
    public ServerResponse addUser(@RequestBody @Validated UserDTO userDTO) {

        User user = convert(userDTO);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return new ServerResponse("fail", null);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userService.insert(user);
        return new ServerResponse("success", null);
    }

    @GetMapping(value = "/findOne/{userId}", produces = "application/json")
    @ResponseBody
    public ServerResponse getUser(@PathVariable("userId") long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            userService.update(user);
            return new ServerResponse("success", user);
        }
        return new ServerResponse("fail", null);
    }

    @GetMapping(value = "/getId", produces = "application/json")
    @ResponseBody
    public Long getLoggedInUserId(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        return userService.getUserIdByUsername(principal.getName());
    }

    @PutMapping(value = "/edit", produces = "application/json")
    @ResponseBody
    public ServerResponse editUser(@RequestBody UserDTO userDTO) {
        User user = convert(userDTO);
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(userService.getUserPasswordById(user.getId()));
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userService.update(user);
        return new ServerResponse("success", null);
    }

    @DeleteMapping(value = "/delete/{userId}", produces = "application/json")
    @ResponseBody
    public ServerResponse deleteUser(@PathVariable("userId") long userId) {
        userService.delete(userId);
        return new ServerResponse("success", null);
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
