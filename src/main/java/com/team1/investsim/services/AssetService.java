package com.team1.investsim.services;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.repositories.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public void saveAsset(AssetEntity assetEntity) {
        assetRepository.saveAndFlush(assetEntity);
    }

    public void saveAllAssets(List<AssetEntity> assetEntities) {
        assetRepository.saveAllAndFlush(assetEntities);
    }

    public List<AssetEntity> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<AssetEntity> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public long countAssets() {
        return assetRepository.count();
    }
}
