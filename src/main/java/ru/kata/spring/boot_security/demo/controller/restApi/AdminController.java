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
import java.util.HashSet;
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
        System.out.println(user.getName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());
        System.out.println(user.getAge());
        System.out.println(user.getPassword());
        System.out.println(user.getRoles());
        User trueUser = new User();
        trueUser.setName(user.getName());
        trueUser.setLastName(user.getLastName());
        trueUser.setEmail(user.getEmail());
        trueUser.setAge(user.getAge());
        trueUser.setPassword(user.getPassword());


        Set<Role> trueRoles = new HashSet<>();
        user.getRoles().stream().forEach(role -> trueRoles.add(userService.getRoleByName(role.getName())));

        System.out.println(trueRoles);


        trueUser.setRoles(trueRoles);

        System.out.println(trueUser.getRoles());
        System.out.println(trueUser);

        if (userService.isEmailUnique(trueUser.getEmail())) {
            userService.saveUser(trueUser);
            return ResponseEntity.ok(trueUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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

    private Set<Role> parseRoles (){
        return null;
    }

}
