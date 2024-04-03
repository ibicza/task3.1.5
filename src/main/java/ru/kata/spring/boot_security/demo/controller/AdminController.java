package ru.kata.spring.boot_security.demo.controller;


import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public static final String REDIRECT_ADD = "redirect:/admin/add";
    public static final String REDIRECT_EDIT = "redirect:/admin/edit?id=";
    public static final String ERROR = "error";
    private UserService userService;


    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        return "addUser";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "editUser";
    }


    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("roles") Optional<Set<Long>> roleIds, RedirectAttributes redirectAttributes) {
        if (userService.isEmailUnique(user.getEmail(), user.getId())) {
            if (roleIds.isPresent()) {
                Set<Role> roles = userService.getRolesByIds(roleIds.get());
                user.setRoles(roles);
                userService.saveUser(user);
                return REDIRECT_HOME;
            } else {
                redirectAttributes.addFlashAttribute(ERROR, "Выберите хотя бы одну роль");
                return REDIRECT_EDIT + user.getId();
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR, "Пользователь с таким адресом электронной почты уже существует");
            return REDIRECT_EDIT + user.getId();
        }
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Optional<Set<Long>> roleIds, RedirectAttributes redirectAttributes) {
        if (userService.isEmailUnique(user.getEmail())) {
            if (roleIds.isPresent()) {
                Set<Role> roles = userService.getRolesByIds(roleIds.get());
                user.setRoles(roles);
                userService.saveUser(user);
                return REDIRECT_HOME;
            } else {
                redirectAttributes.addFlashAttribute(ERROR, "Выберите хотя бы одну роль");
                return REDIRECT_ADD;
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR, "Пользователь с таким адресом электронной почты уже существует");
            return REDIRECT_ADD;
        }
    }


    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return REDIRECT_HOME;
    }
}