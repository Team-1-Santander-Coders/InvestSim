package com.team1.investsim.repositories;

import com.team1.investsim.entities.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<AssetEntity, Long> {}