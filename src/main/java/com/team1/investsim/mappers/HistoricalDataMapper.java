package com.team1.investsim.mappers;

import com.team1.investsim.dtos.HistoricalDataDTO;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.services.HistoricalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoricalDataMapper {
    @Autowired
    private HistoricalDataService historicalDataService;

    public HistoricalDataDTO toDTO(HistoricalDataEntity historicalDataEntity) {
        return createDTO(historicalDataEntity);
    }

    public List<HistoricalDataDTO> toDTO(List<HistoricalDataEntity> historicalDataEntities) {
        List<HistoricalDataDTO> historicalDataDTOs = new ArrayList<HistoricalDataDTO>();
        for (HistoricalDataEntity historicalData : historicalDataEntities) {
            historicalDataDTOs.add(createDTO(historicalData));
        }
        return historicalDataDTOs;
    }

    private HistoricalDataDTO createDTO(HistoricalDataEntity historicalDataEntity) {
        return new HistoricalDataDTO(historicalDataEntity.getId(), historicalDataEntity.getDate(),
                historicalDataEntity.getOpenPrice(), historicalDataEntity.getClosePrice(),
                historicalDataEntity.getHighPrice(), historicalDataEntity.getLowPrice(),
                historicalDataEntity.getVolume());
    }
}
