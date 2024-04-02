package ru.kata.spring.boot_security.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userDao;

    @GetMapping
    public String getUserPage(Model model, Principal principal) {
        model.addAttribute("user", userDao.getUserByName(principal.getName()));
        return "user";
    }
}
