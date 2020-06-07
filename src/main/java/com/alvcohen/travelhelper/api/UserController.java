package com.alvcohen.travelhelper.api;

import com.alvcohen.travelhelper.exception.ResourceNotFoundException;
import com.alvcohen.travelhelper.model.User;
import com.alvcohen.travelhelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getName()));
    }

    @GetMapping("/user/token")
    @PreAuthorize("hasRole('USER')")
    public String getCurrentUserToken(@AuthenticationPrincipal OidcUser user) {
        return user.getIdToken().getTokenValue();
    }
}
