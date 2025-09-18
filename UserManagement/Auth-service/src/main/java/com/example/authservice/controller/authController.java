package com.example.authservice.controller;


import com.example.authservice.Util.JwtUtil;
import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class authController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final UserService userService;
    public authController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;

    }
    @GetMapping("/users")

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtUtil.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new AuthResponse(token, role));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        try {
            User newUser = userService.register(request);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
//        String username = body.get("username");
//        String password = body.get("password");
//
//
//        if (userRepo.findByEmail(email).isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
//        }
//
//        User newUser = new User();
//        newUser.setEmail(email);
//        newUser.setPassword(passwordEncoder.encode(password));
//        newUser.setName(name);
//
//        userRepo.save(newUser);
//
//        String token = jwtService.generateToken(newUser);
//        return ResponseEntity.ok(Map.of("token", token));
//    }
}
