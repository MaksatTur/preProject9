package kg.max.crud.controller;

import kg.max.crud.model.Role;
import kg.max.crud.model.User;
import kg.max.crud.dto.UserDTO;
import kg.max.crud.service.RoleService;
import kg.max.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping()
    public ModelAndView allUsers() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("email", principal.getUsername());
        modelAndView.addObject("userRoles", principal.getRoles());
        modelAndView.setViewName("list");
        return modelAndView;
    }
    @ModelAttribute("roles")
    public List<String> initRoles() {
        return roleService.getAllRolesName();
    }
}
