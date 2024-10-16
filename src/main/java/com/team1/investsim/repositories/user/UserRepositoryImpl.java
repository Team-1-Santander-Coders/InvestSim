package com.team1.investsim.repositories.user;

import com.team1.investsim.entities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntity findByID(long id) {
        return entityManager.find(UserEntity.class, id);
    }

    @Override
    @Transactional
    public void save(UserEntity entity) {
        if (entity.getId() == 0) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        return entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                .getResultList();
    }
}