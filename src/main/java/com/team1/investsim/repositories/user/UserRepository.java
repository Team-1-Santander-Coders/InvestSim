package com.team1.investsim.repositories.user;

import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.repositories.Repository;
import com.team1.investsim.repositories.Updatable;

import java.util.List;

public interface UserRepository extends Repository<UserEntity>, Updatable<UserEntity> {
    @Override
    UserEntity findByID(long id);

    @Override
    void save(UserEntity entity);

    @Override
    List<UserEntity> findAll();

    @Override
    void update(UserEntity UserEntity);
}
