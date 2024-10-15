package com.team1.investsim.repositories.asset;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.repositories.Repository;

import java.util.List;

public interface AssetRepository extends Repository<AssetEntity> {
    @Override
    AssetEntity findByID(long id);

    AssetEntity findByTicker(String assetTicker);

    @Override
    void save(AssetEntity assetEntity);

    @Override
    List<AssetEntity> findAll();

    void saveAll(List<AssetEntity> assetEntities);
}
