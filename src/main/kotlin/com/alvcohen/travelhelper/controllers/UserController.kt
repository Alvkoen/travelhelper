package com.alvcohen.travelhelper.controllers

import com.alvcohen.travelhelper.User
import com.alvcohen.travelhelper.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid
import javax.validation.constraints.Pattern


data class TodoItemAddRequest(val name: String, val description: String?)

data class VisaUsageAddRequest(@field:Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$") val arrival: String,
                               @field:Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$") val departure: String)

@RestController
open class UserController(@Autowired val userService: UserService) {

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    fun getCurrentUser(principal: Principal): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getCurrentUser(principal.name))
    }

    @GetMapping("/user/token")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUserToken(@AuthenticationPrincipal user: OidcUser): String? {
        return user.idToken.tokenValue
    }

    @PostMapping("/user/visa/add")
    @PreAuthorize("isAuthenticated()")
    fun addVisaUsage(principal: Principal, request: @Valid VisaUsageAddRequest): ResponseEntity<Void?>? {
        userService.addVisaUsage(principal.name, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/user/visa/{usageId}/delete")
    @PreAuthorize("isAuthenticated()")
    fun deleteVisaUsage(@PathVariable usageId: Long): ResponseEntity<Void?>? {
        userService.deleteVisaUsageById(usageId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/user/todo/add")
    @PreAuthorize("isAuthenticated()")
    fun addTodoItem(principal: Principal, request: @Valid TodoItemAddRequest): ResponseEntity<Void?>? {
        userService.addTodoItem(principal.name, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/user/todo/{todoId}/delete")
    @PreAuthorize("isAuthenticated()")
    fun deleteTodoItem(@PathVariable todoId: Long): ResponseEntity<Void?>? {
        userService.deleteTodoItemById(todoId)
        return ResponseEntity.ok().build()
    }
}