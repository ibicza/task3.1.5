package ru.kata.spring.boot_security.demo.controller.restApi;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RequestMapping("/api/users")
@RestController
@AllArgsConstructor
public class AdminController {


    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/currentUser")
    public User getCurrentUser(Principal principal){
        return userRepository.getUserByName(principal.getName()).orElse(null);
    }

    @GetMapping("/userList")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/roleList")
    public Set<Role> getAllRoles() {
        return userService.getAllRoles();
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (userService.isEmailUnique(user.getEmail())) {
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.getUserById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody User user) {
        if (userService.isEmailUnique(user.getEmail(), id)) {
            user.setId(id);
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
