package com.example.demo.auth;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.dto.LoginResponseDTO;
import com.example.demo.auth.tokens.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final AuthService authService;

    public AuthController(TokenService tokenService,  AuthService authService) {
        this.tokenService = tokenService;
        this.authService = authService;
    }

        @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> generateToken(@RequestBody LoginEnterDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
