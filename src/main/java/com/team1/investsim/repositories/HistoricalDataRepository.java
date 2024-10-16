package com.team1.investsim.repositories;

import com.team1.investsim.entities.HistoricalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricalDataRepository extends JpaRepository<HistoricalDataEntity, Long> {}
