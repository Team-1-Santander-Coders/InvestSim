package com.team1.investsim.components;

import com.google.common.base.Ticker;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.services.AssetService;
import com.team1.investsim.services.DataLoaderService;
import com.team1.investsim.services.HistoricalDataService;
import com.team1.investsim.utils.CSVProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataAutoLoad {
    @Autowired
    private final AssetService assetService;

    @Autowired
    private final DataLoaderService dataLoaderService;

    public DataAutoLoad(AssetService assetService, DataLoaderService dataLoaderService) {
        this.assetService = assetService;
        this.dataLoaderService = dataLoaderService;
        /*
        if (assetService.countAssets() <= 10) {
            System.out.println("Loading data...");
            loadAssetsAndHistoricalData();
        }*/
    }

    private void loadAssetsAndHistoricalData() {
        String companyNamesPath = "src/main/resources/data/empresas.csv";
        List<String[]> tickersList = CSVProcessor.processCSV(companyNamesPath).get();
        for (String[] ticker : tickersList) {
            AssetEntity asset = new AssetEntity();
            if (ticker[0].equalsIgnoreCase("mmm")) {
                asset.setId(1L);
            }

            asset.setTicker(ticker[0]);
            String dataToLoadPath = "src/main/resources/data/sp500_data.csv";
            dataLoaderService.loadData(assetService.saveAsset(asset), dataToLoadPath);
        }
        System.out.println("Data loaded.");
    }

}
