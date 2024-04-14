package ru.kata.spring.boot_security.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    public static final String REDIRECT_ADMIN = "redirect:/admin";
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public String adminPage(Model model, Principal principal) {
        model.addAttribute("activeUser", userRepository.getUserByName(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", userService.getAllRoles());
        return "adminPage";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, @RequestParam("roles") Optional<Set<Long>> roleIds, RedirectAttributes redirectAttributes) {
        if (userService.isEmailUnique(user.getEmail())) {
            roleIds.ifPresent(ids -> user.setRoles(userService.getRolesByIds(ids)));
            userService.saveUser(user);
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("email", user.getEmail());
        }
        return REDIRECT_ADMIN;
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, @RequestParam("roles") Optional<Set<Long>> roleIds) {
        if (userService.isEmailUnique(user.getEmail(), user.getId())) {
            roleIds.ifPresent(ids -> user.setRoles(userService.getRolesByIds(ids)));
            userService.saveUser(user);
        }
        return REDIRECT_ADMIN;
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return REDIRECT_ADMIN;
    }

}
