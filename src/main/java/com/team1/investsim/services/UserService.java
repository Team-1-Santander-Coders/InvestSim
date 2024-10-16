package com.team1.investsim.services;

import com.team1.investsim.entities.AssetHoldingEntity;
import com.team1.investsim.entities.TransactionEntity;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.exceptions.AssetHoldingNotFoundException;
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

    public void saveUser(UserEntity userEntity) {
        if (isRegistered(userEntity)) {
            try {
                userEntity.setId(getUserByDocument(userEntity.getDocument()).getId());
            } catch (DocumentNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public void removeAssetHolding(UserEntity userEntity, AssetHoldingEntity assetHoldingEntity) throws AssetHoldingNotFoundException {
        userEntity.getPortfolio().removeAssetHolding(assetHoldingEntity);
        userRepository.saveAndFlush(userEntity);
    }

    public void addAssetHolding(UserEntity userEntity, AssetHoldingEntity assetHoldingEntity) {
        userEntity.getPortfolio().addAssetHolding(assetHoldingEntity);
        userRepository.saveAndFlush(userEntity);
    }

    public void addTransaction(UserEntity userEntity, TransactionEntity transactionEntity) {
        userEntity.getPortfolio().addTransaction(transactionEntity);
        userRepository.saveAndFlush(userEntity);
    }

    public boolean isRegistered(UserEntity userEntity) {
        return userRepository.findAll().stream().anyMatch(registeredUser -> registeredUser.equals(userEntity));
    }
}
