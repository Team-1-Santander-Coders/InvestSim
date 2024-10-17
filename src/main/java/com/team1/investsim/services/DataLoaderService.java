package com.team1.investsim.services;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.utils.CSVProcessor;
import com.team1.investsim.utils.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataLoaderService {

    public static void loadData(AssetEntity assetEntity, String filepath) {
        Optional<List<String[]>> assetsDataOptional = CSVProcessor.processCSV(filepath);

        if (assetsDataOptional.isPresent()) {
            List<String[]> assetsData = assetsDataOptional.get();
            List<HistoricalDataEntity> historicalDataEntityList = new ArrayList<>();

            assetsData.stream()
                    .filter(assetData -> assetEntity.getTicker().equals(assetData[7]))
                    .forEach(assetData -> {
                        try {
                            HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity(
                                    DateUtil.stringToDate(assetData[0],"yyyy-MM-dd"),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[1])),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[2])),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[3])),
                                    BigDecimal.valueOf(Double.parseDouble(assetData[4])),
                                    Double.valueOf(assetData[5]).longValue(),
                                    assetEntity);
                            historicalDataEntityList.add(historicalDataEntity);

                        } catch (Exception e) { e.printStackTrace(); }
                    });

            if(!historicalDataEntityList.isEmpty()) assetEntity.setHistoricalData(historicalDataEntityList);
        }
    }
}
