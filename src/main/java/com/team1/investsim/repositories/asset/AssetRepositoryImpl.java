package com.team1.investsim.repositories.asset;

import com.team1.investsim.entities.AssetEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssetRepositoryImpl implements AssetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AssetEntity findByID(long id) {
        return entityManager.find(AssetEntity.class, id);
    }

    @Override
    @Transactional
    public void save(AssetEntity assetEntity) {
        if (assetEntity.getId() == 0) {
            entityManager.persist(assetEntity);
        } else {
            entityManager.merge(assetEntity);
        }
    }

    @Override
    public List<AssetEntity> findAll() {
        return entityManager.createQuery("SELECT a FROM AssetEntity a", AssetEntity.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void saveAll(List<AssetEntity> assetEntities) {
        for (AssetEntity assetEntity : assetEntities) {
            if (assetEntity.getId() == 0) {
                entityManager.persist(assetEntity);
            } else {
                entityManager.merge(assetEntity);
            }
        }
    }
}
