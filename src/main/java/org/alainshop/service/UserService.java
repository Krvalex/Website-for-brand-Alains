package org.alainshop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.alainshop.model.User;
import org.alainshop.model.enums.Role;
import org.alainshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void create(User user) {
        if (existsByUsername(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
    }

    public User getByPrincipal(Principal principal) {
        if (principal == null) return null;
        return userRepository.findByEmail(principal.getName()).orElseThrow();
    }

    public boolean existsByUsername(String email) {
        return userRepository.existsByEmail(email);
    }
}