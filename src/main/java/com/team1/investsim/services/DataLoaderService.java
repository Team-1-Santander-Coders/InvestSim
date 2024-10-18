package com.team1.investsim.services;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.utils.CSVProcessor;
import com.team1.investsim.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DataLoaderService {
    @Autowired
    private HistoricalDataService historicalDataService;

    public void loadData(AssetEntity assetEntity, String filepath) {
        Optional<List<String[]>> assetsDataOptional = CSVProcessor.processCSV(filepath);

        if (assetsDataOptional.isPresent()) {
            List<String[]> assetsData = assetsDataOptional.get();

            assetsData.stream()
                    .filter(assetData -> assetEntity.getTicker().equals(assetData[7]))
                    .forEach(assetData -> {
                        try {
                            HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity(
                                    DateUtil.stringToDate(assetData[0], DateUtil.CSV_DATE_PATTERN),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[1])),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[2])),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[3])),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[4])),
                                    Double.valueOf(assetData[5]).longValue(),
                                    assetEntity);

                            historicalDataService.saveHistoricalData(historicalDataEntity);

                        } catch (Exception e) { e.printStackTrace(); }});
        }
    }
}

