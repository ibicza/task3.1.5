package ru.kata.spring.boot_security.demo.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;



import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.kata.spring.boot_security.demo.service.impl.UserDetailServiceImpl;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final SuccessUserHandler successUserHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(auth -> auth// Allow access to login page
                        .requestMatchers("/login").permitAll()
                        // Restrict access to user API to authenticated users
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        // Restrict access to admin API to admins only
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Allow access to static resources
                        .requestMatchers("/static/**").permitAll()
                        // Allow access to other endpoints for authenticated users
                        .anyRequest().authenticated()).
                formLogin(form -> form.successHandler(successUserHandler).loginPage("/login").permitAll())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

