package com.alvcohen.travelhelper.controllers

import com.alvcohen.travelhelper.AuthProvider
import com.alvcohen.travelhelper.User
import com.alvcohen.travelhelper.UserRepository
import com.alvcohen.travelhelper.VisaUsage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpRequest(@field:NotBlank val name: String,
                         @field:Email @field:NotBlank val email: String,
                         @field:NotBlank val password: String)

class AuthController(@Autowired val userRepository: UserRepository, @Autowired val passwordEncoder: PasswordEncoder) {

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignUpRequest): ResponseEntity<*>? {
        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(java.lang.String.format("Email: %s already exists", signUpRequest.email))
        }

        val user = User(null,
                signUpRequest.email,
                signUpRequest.name,
                false,
                mutableSetOf(),
                mutableSetOf(),
                passwordEncoder.encode(signUpRequest.password),
                AuthProvider.LOCAL,
                null)

        val result = userRepository.save(user)
        val location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.id).toUri()
        return ResponseEntity.created(location).build<Any>()
    }

}