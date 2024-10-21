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

    public HistoricalDataEntity saveHistoricalData(HistoricalDataEntity historicalData) {
        if (isHistoricalDataRegistered(historicalData)) {
            long id = getAlreadyRegisteredHistoricalData(historicalData).get().getId();
            historicalData.setId(id);
            return historicalDataRepository.saveAndFlush(historicalData);
        }
        return historicalDataRepository.saveAndFlush(historicalData);
    }

    public List<HistoricalDataEntity> saveAllHistoricalData(List<HistoricalDataEntity> historicalData) {
        return historicalDataRepository.saveAllAndFlush(historicalData);
    }

    public List<HistoricalDataEntity> getAllHistoricalData() {
        return historicalDataRepository.findAll();
    }

    public Optional<HistoricalDataEntity> getHistoricalDataById(Long id) {
        return historicalDataRepository.findById(id);
    }

    public Optional<HistoricalDataEntity> getAlreadyRegisteredHistoricalData(HistoricalDataEntity historicalData) {
        return historicalDataRepository.findAll().stream()
                .filter(registeredHistoricalData -> registeredHistoricalData.equals(historicalData))
                .findFirst();
    }

    public boolean isHistoricalDataRegistered(HistoricalDataEntity historicalData) {
        return historicalDataRepository.findAll().stream().anyMatch(registeredHistoricalData -> registeredHistoricalData.equals(historicalData));
    }

    public long countHistoricalData() {
        return historicalDataRepository.count();
    }

}
