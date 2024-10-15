package com.team1.investsim.repositories.user;

import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.repositories.Repository;
import com.team1.investsim.repositories.Updatable;

public interface UserRepository extends Repository<UserEntity>, Updatable<UserEntity> {}
