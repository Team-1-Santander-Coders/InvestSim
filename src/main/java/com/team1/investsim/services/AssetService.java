package com.team1.investsim.services;

import com.team1.investsim.dtos.AssetDTO;
import com.team1.investsim.dtos.AssetSimpleDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.exceptions.TickerNotFoundException;
import com.team1.investsim.mappers.AssetMapper;
import com.team1.investsim.repositories.AssetRepository;

import com.team1.investsim.repositories.HistoricalDataRepository;
import com.team1.investsim.utils.DataAssetPredictionUtil;
import com.team1.investsim.utils.ThrowingFunctionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private HistoricalDataRepository historicalDataRepository;

    public AssetEntity saveAsset(AssetEntity assetEntity) {
        return assetRepository.saveAndFlush(assetEntity);
    }

    public void saveAllAssets(List<AssetEntity> assetEntities) {
        assetRepository.saveAllAndFlush(assetEntities);
    }

    public List<AssetEntity> getAllAssets() {
        return assetRepository.findAllAssetsWithHistoricalData();
    }

    public List<AssetSimpleDTO> getAllAssetsAsDTO() {
        return assetRepository.findAllAssetsAsDTO();
    }

    public Optional<AssetEntity> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public AssetEntity getAssetByTicker(String ticker) throws TickerNotFoundException {
        return assetRepository.findAll().stream()
                .filter(assetEntity -> assetEntity.getTicker().equals(ticker))
                .findFirst().orElseThrow(() -> new TickerNotFoundException("Ticker não encontrado no sistema."));
    }

    public long getAssetIdByTicker(String ticker) throws TickerNotFoundException {
        return getAssetByTicker(ticker).getId();
    }

    public List<HistoricalDataEntity> getHistoricalDataByPeriod(long assetId, LocalDateTime start, LocalDateTime end) throws HistoricalDataNotFoundException {
        List<HistoricalDataEntity> historicalDataEntities = historicalDataRepository.findHistoricalDataByAssetAndDateRange(assetId, start, end);

        if (historicalDataEntities.isEmpty()) {
            AssetEntity asset = assetRepository.findById(assetId)
                    .orElseThrow(() -> new HistoricalDataNotFoundException("Ativo não encontrado com ID: " + assetId));


            HistoricalDataEntity predictedData = ThrowingFunctionWrapper.wrap(item ->
                    DataAssetPredictionUtil.predictAssetHistoricalData(asset, start)).apply(asset);

            return Collections.singletonList(predictedData);
        }

        return historicalDataEntities;
    }

    public HistoricalDataEntity getHistoricalDataByDate(long assetId, LocalDateTime date) throws HistoricalDataNotFoundException {

        return historicalDataRepository.findHistoricalDataByAssetAndDate(assetId, date)
                .orElseGet(() -> {
                    AssetEntity asset = null;
                    try {
                        asset = assetRepository.findById(assetId)
                                .orElseThrow(() -> new HistoricalDataNotFoundException("Ativo não encontrado com ID: " + assetId));
                    } catch (HistoricalDataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return DataAssetPredictionUtil.predictAssetHistoricalData(asset, date);
                });
    }

    public long countAssets() {
        return assetRepository.count();
    }
}
