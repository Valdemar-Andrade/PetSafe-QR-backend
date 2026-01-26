package com.petsafe.qr.service;

import com.petsafe.qr.dto.AuthResponse;
import com.petsafe.qr.dto.LoginRequest;
import com.petsafe.qr.dto.RegisterRequest;
import com.petsafe.qr.entity.User;
import com.petsafe.qr.exception.BadRequestException;
import com.petsafe.qr.repository.UserRepository;
import com.petsafe.qr.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        
        User savedUser = userRepository.save(user);
        
        String token = tokenProvider.generateTokenFromUserId(savedUser.getId());
        
        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }
    
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
        
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }
}