package com.team1.investsim.repositories.assetholding;

import com.team1.investsim.entities.AssetHoldingEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssetHoldingRepositoryImpl implements AssetHoldingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AssetHoldingEntity findByID(long id) {
        return entityManager.find(AssetHoldingEntity.class, id);
    }

    @Override
    @Transactional
    public void save(AssetHoldingEntity assetHoldingEntity) {
        if (assetHoldingEntity.getId() == 0) {
            entityManager.persist(assetHoldingEntity);
        } else {
            entityManager.merge(assetHoldingEntity);
        }
    }

    @Override
    public List<AssetHoldingEntity> findAll() {
        TypedQuery<AssetHoldingEntity> query = entityManager.createQuery(
                "SELECT ah FROM AssetHoldingEntity ah", AssetHoldingEntity.class);
        return query.getResultList();
    }
}
