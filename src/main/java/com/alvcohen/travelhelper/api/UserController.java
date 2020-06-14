package com.alvcohen.travelhelper.api;

import com.alvcohen.travelhelper.dto.TodoItemAddRequest;
import com.alvcohen.travelhelper.dto.VisaUsageAddRequest;
import com.alvcohen.travelhelper.model.User;
import com.alvcohen.travelhelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getCurrentUser(final Principal principal) {
        return ResponseEntity.ok(userService.getCurrentUser(principal.getName()));
    }

    @GetMapping("/user/token")
    @PreAuthorize("hasRole('USER')")
    public String getCurrentUserToken(@AuthenticationPrincipal final OidcUser user) {
        return user.getIdToken().getTokenValue();
    }

    @PostMapping("/user/visa/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addVisaUsage(final Principal principal, @Valid final VisaUsageAddRequest request) {
        userService.addVisaUsage(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/visa/{usageId}/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteVisaUsage(@PathVariable final long usageId) {
        userService.deleteVisaUsageById(usageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/todo/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addTodoItem(final Principal principal, @Valid final TodoItemAddRequest request) {
        userService.addTodoItem(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/todo/{todoId}/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable final long todoId) {
        userService.deleteTodoItemById(todoId);
        return ResponseEntity.ok().build();
    }
}
