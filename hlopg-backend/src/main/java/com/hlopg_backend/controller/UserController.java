package com.hlopg_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hlopg_backend.dto.ChangePasswordRequest;
import com.hlopg_backend.model.User;
import com.hlopg_backend.repository.UserRepository;
import com.hlopg_backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest request) {

        // 1️⃣ Extract token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Missing token"));
        }

        String token = authHeader.substring(7);

        // 2️⃣ Validate token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Invalid token"));
        }

        // 3️⃣ Get user from DB
        Long userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4️⃣ Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Current password is incorrect"));
        }

        // 5️⃣ Save new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("success", true, "message", "Password changed successfully"));
    }
}
