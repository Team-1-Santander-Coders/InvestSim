package com.team1.investsim.mappers;

import com.team1.investsim.dtos.UserDTO;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.exceptions.DocumentNotFoundException;
import com.team1.investsim.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {
    @Autowired
    private UserService userService;

    public Optional<UserEntity> toEntity(UserDTO userDTO) {
        try {
            return Optional.ofNullable(userService.getUserByDocument(userDTO.document()));
        } catch (DocumentNotFoundException e) {
            return Optional.empty();
        }
    }
}
