package com.team1.investsim.repositories;

import com.team1.investsim.entities.HistoricalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HistoricalDataRepository extends JpaRepository<HistoricalDataEntity, Long> {
    @Query("SELECT h FROM HistoricalDataEntity h WHERE h.asset.id = :assetId AND h.date BETWEEN :start AND :end")
    List<HistoricalDataEntity> findHistoricalDataByAssetAndDateRange(
            @Param("assetId") long assetId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT h FROM HistoricalDataEntity h WHERE h.asset.id = :assetId AND h.date = :date")
    Optional<HistoricalDataEntity> findHistoricalDataByAssetAndDate(
            @Param("assetId") long assetId,
            @Param("date") LocalDateTime date);
}
