package com.team1.investsim.services;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.utils.CSVProcessor;
import com.team1.investsim.utils.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataLoader {

    public static void loadData(AssetEntity assetEntity, String filepath) {
        Optional<List<String[]>> assetsDataOptional = CSVProcessor.processCSV(filepath);

        if (assetsDataOptional.isPresent()) {
            List<String[]> assetsData = assetsDataOptional.get();
            List<HistoricalDataEntity> historicalDataEntityList = new ArrayList<>();
            try {

                for (String[] assetData : assetsData) {
                    if(assetEntity.getTicker().equals(assetData[7])) {
                        HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity();
                        historicalDataEntity.setAll(assetEntity, assetData[0], assetData[1], assetData[2], assetData[3], assetData[4], assetData[5]);
                        historicalDataEntityList.add(historicalDataEntity);
                        
                        assetEntity.setHistoricalData(historicalDataEntityList);
                    }
                }

            } catch (IllegalDateException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setTicker("MMM");

        loadData(assetEntity, "sp500_data.csv");

        System.out.println("ticker: " + assetEntity.getTicker() + " - historicalDataLength: "+assetEntity.getHistoricalData().size());
    }
}
