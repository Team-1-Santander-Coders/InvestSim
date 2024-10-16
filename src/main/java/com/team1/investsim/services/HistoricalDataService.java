package com.team1.investsim.services;

import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.repositories.HistoricalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoricalDataService {
    @Autowired
    private HistoricalDataRepository historicalDataRepository;

    public void saveHistoricalData(HistoricalDataEntity historicalData) {
        historicalDataRepository.saveAndFlush(historicalData);
    }

    public void saveAllHistoricalData(List<HistoricalDataEntity> historicalData) {
        historicalDataRepository.saveAllAndFlush(historicalData);
    }

    public List<HistoricalDataEntity> getAllHistoricalData() {
        return historicalDataRepository.findAll();
    }

    public Optional<HistoricalDataEntity> getHistoricalDataById(Long id) {
        return historicalDataRepository.findById(id);
    }

    public long countHistoricalData() {
        return historicalDataRepository.count();
    }

}
