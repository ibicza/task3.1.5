package ru.kata.spring.boot_security.demo.model;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;


import java.util.Set;

public class RoleEditor extends CustomCollectionEditor {
    public RoleEditor() {
        super(Set.class);
    }

    @Override
    protected Object convertElement(Object element) {
        Long id = Long.parseLong((String) element);
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
