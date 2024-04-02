package ru.kata.spring.boot_security.demo.controller;


import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.util.Optional;
import java.util.Set;



@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {


    public static final String REDIRECT_HOME = "redirect:/admin";
    private UserService userDao;


    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userDao.getAllUsers());
        return "userList";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userDao.getAllRoles());
        return "addUser";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> user = userDao.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userDao.getAllRoles());
        return "editUser";
    }


    @PostMapping({"/edit", "/add"})
    public String editUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds) {
        Set<Role> roles = userDao.getRolesByIds(roleIds);
        user.setRoles(roles);
        userDao.saveUser(user);
        return REDIRECT_HOME;
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userDao.deleteUser(id);
        return REDIRECT_HOME;
    }
}