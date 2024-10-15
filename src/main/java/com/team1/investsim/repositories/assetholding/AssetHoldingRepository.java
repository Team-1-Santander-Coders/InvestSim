package com.team1.investsim.repositories.assetholding;

import com.team1.investsim.entities.AssetHoldingEntity;
import com.team1.investsim.repositories.Repository;

import java.util.List;

public interface AssetHoldingRepository extends Repository<AssetHoldingEntity> {
    @Override
    AssetHoldingEntity findByID(long id);

    @Override
    void save(AssetHoldingEntity assetHolding);

    @Override
    List<AssetHoldingEntity> findAll();
}
