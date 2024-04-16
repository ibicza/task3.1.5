package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(User user);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByName(String name);

    void deleteUser(Long id);

    Set<Role> getAllRoles();

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    Role saveRole(Role role);

    void deleteRole(Long id);

    Set<Role> getRolesByIds(Set<Long> ids);

    boolean isEmailUnique(String email);

    boolean isEmailUnique(String email, Long exceptId);


}
