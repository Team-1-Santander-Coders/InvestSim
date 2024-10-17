package com.team1.investsim.services;

import com.team1.investsim.entities.TransactionEntity;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.exceptions.DocumentNotFoundException;
import com.team1.investsim.exceptions.EmailNotFoundException;
import com.team1.investsim.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveTransaction(UserEntity userEntity) {
        userRepository.saveAndFlush(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity getUserByEmail(String email) throws EmailNotFoundException {
        return userRepository.findAll().stream()
                .filter(userEntity -> userEntity.getEmail().equals(email)).findFirst().orElseThrow(() -> new EmailNotFoundException("Email não encontrado no banco de dados."));
    }

    public UserEntity getUserByDocument(String document) throws DocumentNotFoundException {
        return userRepository.findAll().stream()
                .filter(userEntity -> userEntity.getDocument().equals(document)).findFirst().orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado no banco de dados."));
    }

    public boolean validateUserLogin(String email, String password) throws EmailNotFoundException {
        return getUserByEmail(email).getPassword().equals(password);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
