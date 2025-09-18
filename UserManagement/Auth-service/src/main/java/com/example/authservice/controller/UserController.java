package com.example.authservice.controller;

import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // chỉ ADMIN mới tạo user
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> createUser(@RequestBody User user) {
//        User saved = userService.createUser(user);
//        return ResponseEntity.ok(saved);
//    }
}
