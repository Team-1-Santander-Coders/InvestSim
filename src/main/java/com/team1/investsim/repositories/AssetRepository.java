package com.team1.investsim.repositories;

import com.team1.investsim.dtos.AssetSimpleDTO;
import com.team1.investsim.entities.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    @Query("SELECT a FROM AssetEntity a JOIN FETCH a.historicalData")
    List<AssetEntity> findAllAssetsWithHistoricalData();

    @Query("SELECT new com.team1.investsim.dtos.AssetSimpleDTO(a.id, a.ticker) FROM AssetEntity a")
    List<AssetSimpleDTO> findAllAssetsAsDTO();
}