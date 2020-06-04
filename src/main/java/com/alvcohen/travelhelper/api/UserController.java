package com.alvcohen.travelhelper.api;

import com.alvcohen.travelhelper.config.security.CurrentUser;
import com.alvcohen.travelhelper.config.security.UserPrincipal;
import com.alvcohen.travelhelper.exception.ResourceNotFoundException;
import com.alvcohen.travelhelper.model.User;
import com.alvcohen.travelhelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
