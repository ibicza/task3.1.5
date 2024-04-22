package ru.kata.spring.boot_security.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByName(String name);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(Long id);
}
