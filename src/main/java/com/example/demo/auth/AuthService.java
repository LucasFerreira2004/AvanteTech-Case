package com.example.demo.auth;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.exceptions.InvalidCredentialsException;
import com.example.demo.auth.tokens.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private TokenService tokenService;

    public LoginResponseDTO login(LoginEnterDTO dto){
        String adminEmail = "avante@gmail.com";
        String adminPassword = "avante";
        if (!dto.email().equals(adminEmail))
            throw new InvalidCredentialsException("email");
        if (!dto.password().equals(adminPassword))
            throw new InvalidCredentialsException("password");

        String token = tokenService.generateToken(dto);
        return new LoginResponseDTO(token);
    }
}
