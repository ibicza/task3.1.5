package ru.kata.spring.boot_security.demo.controller.restApi;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


import java.security.Principal;


@RequestMapping("/api/user")
@RestController
@AllArgsConstructor
public class UserApiController {

    private final UserRepository userRepository;

    @GetMapping("/currentUser")
    public User getCurrentUser(Principal principal){
        return userRepository.getUserByName(principal.getName()).orElse(null);
    }
}
