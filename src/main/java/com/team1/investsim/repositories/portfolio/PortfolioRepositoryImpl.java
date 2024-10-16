package com.team1.investsim.repositories.portfolio;

import com.team1.investsim.entities.PortfolioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortfolioRepositoryImpl implements PortfolioRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PortfolioEntity findByID(long id) {
        return entityManager.find(PortfolioEntity.class, id);
    }

    @Override
    @Transactional
    public void save(PortfolioEntity entity) {
        if (entity.getId() == 0) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Override
    public List<PortfolioEntity> findAll() {
        TypedQuery<PortfolioEntity> query = entityManager.createQuery(
                "SELECT p FROM PortfolioEntity p", PortfolioEntity.class);
        return query.getResultList();
    }
}
