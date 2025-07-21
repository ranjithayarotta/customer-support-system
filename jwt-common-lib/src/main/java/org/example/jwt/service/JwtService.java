package org.example.jwt.service;

import org.example.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String createToken(String username, String role) {
        return jwtUtil.generateToken(username, role);
    }

    public boolean isValid(String token) {
        return jwtUtil.validateToken(token);
    }

    public String getUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    public String getRole(String token) {
        return jwtUtil.extractUserRole(token);
    }
}