package com.team1.investsim.repositories;

import com.team1.investsim.entities.AssetHoldingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetHoldingRepository extends JpaRepository<AssetHoldingEntity, Long> {}
