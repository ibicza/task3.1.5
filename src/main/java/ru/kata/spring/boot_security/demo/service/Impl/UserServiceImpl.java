package ru.kata.spring.boot_security.demo.service.Impl;



import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(repository.getReferenceById(id));
    }


    public Optional<User> getUserByName(String name) {
        return repository.getUserByName(name);
    }


    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public Set<Role> getAllRoles() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> rootEntry = cq.from(Role.class);
        CriteriaQuery<Role> all = cq.select(rootEntry);
        TypedQuery<Role> allQuery = entityManager.createQuery(all);
        return new HashSet<>(allQuery.getResultList());
    }

    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    public Set<Role> getRolesByIds(Set<Long> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> root = cq.from(Role.class);
        cq.select(root).where(root.get("id").in(ids));
        TypedQuery<Role> query = entityManager.createQuery(cq);
        return new HashSet<>(query.getResultList());
    }


    public Role saveRole(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
            return role;
        } else {
            return entityManager.merge(role);
        }
    }

    public void deleteRole(Long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

}
