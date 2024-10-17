package com.team1.investsim.services;

import com.team1.investsim.entities.PortfolioEntity;
import com.team1.investsim.repositories.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public void savePortfolio(PortfolioEntity portfolioEntity) {
        portfolioRepository.saveAndFlush(portfolioEntity);
    }

    public List<PortfolioEntity> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Optional<PortfolioEntity> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    public long countPortfolios() {
        return portfolioRepository.count();
    }
}
