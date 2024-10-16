package com.team1.investsim.services;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.utils.CSVProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataLoader {

    public static void loadData(AssetEntity assetEntity, String filepath) {
        Optional<List<String[]>> assetsDataOptional = CSVProcessor.processCSV(filepath);

        if (assetsDataOptional.isPresent()) {
            List<String[]> assetsData = assetsDataOptional.get();
            List<HistoricalDataEntity> historicalDataEntityList = new ArrayList<>();

            assetsData.stream()
                    .filter(assetData -> assetEntity.getTicker().equals(assetData[7]))
                    .map(assetData -> {
                        try {
                            HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity();
                            historicalDataEntity.setAll(assetEntity, assetData[0], assetData[1], assetData[2], assetData[3], assetData[4], assetData[5]);
                            historicalDataEntityList.add(historicalDataEntity);

                        } catch (Exception e) { e.printStackTrace(); }

                        return historicalDataEntityList;})
                    .collect(Collectors.toList());

            if(!historicalDataEntityList.isEmpty()) assetEntity.setHistoricalData(historicalDataEntityList);

        }
    }
}
