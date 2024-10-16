package com.team1.investsim.test;

import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.services.AssetService;
import com.team1.investsim.services.HistoricalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TestCadastro {

    private final AssetService assetService;
    private final HistoricalDataService historicalDataService;

    @Autowired
    public TestCadastro(AssetService assetService, HistoricalDataService historicalDataService) {
        this.assetService = assetService;
        this.historicalDataService = historicalDataService;
    }

    public void testeCriarEntidades() {
        AssetEntity asset = new AssetEntity();
        asset.setTicker("Teste asset");

        assetService.saveAsset(asset);

        System.out.println(assetService.getAllAssets());

        HistoricalDataEntity historicalData = new HistoricalDataEntity(
                LocalDateTime.now(),
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.TEN,
                1000000L,
                asset
        );

        historicalDataService.saveHistoricalData(historicalData);

        System.out.println(historicalDataService.getAllHistoricalData());
    }
}
