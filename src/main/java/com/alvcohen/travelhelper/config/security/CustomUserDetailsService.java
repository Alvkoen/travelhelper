package com.alvcohen.travelhelper.config.security;

import com.alvcohen.travelhelper.exception.ResourceNotFoundException;
import com.alvcohen.travelhelper.model.User;
import com.alvcohen.travelhelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with email %s not found", email))
            );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
