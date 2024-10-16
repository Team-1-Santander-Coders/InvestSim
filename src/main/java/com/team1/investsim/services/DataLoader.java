package com.team1.investsim.services;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.repositories.asset.AssetRepository;
import com.team1.investsim.repositories.asset.AssetRepositoryImpl;
import com.team1.investsim.utils.CSVProcessor;
import com.team1.investsim.utils.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataLoader {
    AssetRepository assetRepository;

    public void loadData(String filepath) {
        assetRepository = new AssetRepositoryImpl();
        List<AssetEntity> assetEntityList = assetRepository.findAll();

        Optional<List<String[]>> assetsDataOptional = CSVProcessor.processCSV(filepath);

        if (assetsDataOptional.isPresent()) {
            List<String[]> assetsData = assetsDataOptional.get();

            assetEntityList.stream().forEach(asset -> {
                for (AssetEntity assetEntity : assetEntityList) {
                    List<HistoricalDataEntity> historicalDataEntityList = new ArrayList<>();
                    for (String[] assetData : assetsData) {
                        if (assetData[7].equals(asset.getTicker())) {

                            try {
                                HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity();
                                historicalDataEntity.setAsset(assetEntity);
                                historicalDataEntity.setDate(DateUtil.stringToDate(assetData[0], "yyyy-MM-dd"));
                                historicalDataEntity.setOpenPrice(BigDecimal.valueOf(Double.valueOf(assetData[1])));
                                historicalDataEntity.setHighPrice(BigDecimal.valueOf(Double.valueOf(assetData[2])));
                                historicalDataEntity.setLowPrice(BigDecimal.valueOf(Double.valueOf(assetData[3])));
                                historicalDataEntity.setClosePrice(BigDecimal.valueOf(Double.valueOf(assetData[4])));
                                historicalDataEntity.setVolume(Double.valueOf(assetData[6]).longValue());
                                historicalDataEntityList.add(historicalDataEntity);

                            } catch (IllegalDateException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    assetEntity.setHistoricalData(historicalDataEntityList);
                }
            });
        }
    }
}
