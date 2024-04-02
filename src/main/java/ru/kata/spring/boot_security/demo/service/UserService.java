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

    public Optional<User> getUserByName(String name);

    void deleteUser(Long id);

    public Set<Role> getAllRoles();

    public Role getRoleById(Long id);

    public Role saveRole(Role role);

    public void deleteRole(Long id);

    public Set<Role> getRolesByIds(Set<Long> ids);



}
