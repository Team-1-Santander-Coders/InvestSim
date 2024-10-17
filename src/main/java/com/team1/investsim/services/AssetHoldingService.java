package com.team1.investsim.services;

import com.team1.investsim.entities.AssetHoldingEntity;
import com.team1.investsim.repositories.AssetHoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetHoldingService {
    @Autowired
    private AssetHoldingRepository assetHoldingRepository;

    public void saveAssetHolding(AssetHoldingEntity assetHoldingEntity) {
        assetHoldingRepository.saveAndFlush(assetHoldingEntity);
    }

    public List<AssetHoldingEntity> getAllAssetHoldings() {
        return assetHoldingRepository.findAll();
    }

    public Optional<AssetHoldingEntity> getAssetHoldingById(Long id) {
        return assetHoldingRepository.findById(id);
    }

    public long countAssetHoldings() {
        return assetHoldingRepository.count();
    }
}
