package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import ru.kata.spring.boot_security.demo.model.RoleEditor;

import java.util.Set;

@ControllerAdvice
public class GlobalControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Set.class, "roles", new RoleEditor());
    }
}
