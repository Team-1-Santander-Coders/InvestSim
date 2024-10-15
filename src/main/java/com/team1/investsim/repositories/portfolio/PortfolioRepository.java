package com.team1.investsim.repositories.portfolio;

import com.team1.investsim.entities.PortfolioEntity;
import com.team1.investsim.repositories.Repository;
import com.team1.investsim.repositories.Updatable;

import java.util.List;

public interface PortfolioRepository extends Repository<PortfolioEntity>, Updatable<PortfolioEntity> {
    @Override
    PortfolioEntity findByID(long id);

    @Override
    void save(PortfolioEntity portfolioEntity);

    @Override
    List<PortfolioEntity> findAll();

    @Override
    void update(PortfolioEntity portfolioEntity);
}
