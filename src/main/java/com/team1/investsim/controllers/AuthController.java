package com.team1.investsim.controllers;

import com.team1.investsim.components.JwtManager;
import com.team1.investsim.dtos.LoginRequestDTO;
import com.team1.investsim.dtos.RegisterRequestDTO;
import com.team1.investsim.dtos.ResponseDTO;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.exceptions.*;
import com.team1.investsim.services.UserService;
import com.team1.investsim.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtManager jwtManager;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtManager jwtManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtManager = jwtManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        try {
            Optional<UserEntity> user = validateUserLogin(body.email(), body.password());
            if (user.isEmpty()) throw new AuthenticationFailedException("Usuário ou senha incorretos");
            else return ResponseEntity.ok(new ResponseDTO(this.jwtManager.generateToken(user.get()), user.get().getEmail()));
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email não encontrado no banco de dados.");
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        try {
            String encryptedPassword = passwordEncoder.encode(body.password());
            UserEntity user = userService.registerUser(body.document(), body.email(), encryptedPassword);
            return ResponseEntity.ok(new ResponseDTO(this.jwtManager.generateToken(user), user.getEmail()));
        } catch (InvalidPasswordException | InvalidEmailException | InvalidDocumentException | AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DuplicatedUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    private Optional<UserEntity> validateUserLogin(String email, String password) throws EmailNotFoundException {
        UserEntity user = userService.getUserByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) return Optional.of(user);
        else return Optional.empty();
    }
}
