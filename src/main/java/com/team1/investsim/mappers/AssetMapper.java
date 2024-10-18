package com.team1.investsim.mappers;

import com.team1.investsim.dtos.AssetDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.services.AssetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AssetMapper {
    @Autowired
    private AssetService assetService;

    public AssetDTO toDto(AssetEntity assetEntity) throws HistoricalDataNotFoundException {
        HistoricalDataEntity historicalDataEntity = assetEntity.getHistoricalDataByDate(LocalDate.now().atStartOfDay());

        return new AssetDTO(assetEntity.getId(),
                            assetEntity.getTicker(),
                            assetEntity.getValueByDate(LocalDate.now().atStartOfDay()),
                            assetEntity.getDailyChange(LocalDate.now().atStartOfDay()),
                            historicalDataEntity.getOpenPrice(),
                            historicalDataEntity.getClosePrice(),
                            historicalDataEntity.getHighPrice(),
                            historicalDataEntity.getLowPrice(),
                            historicalDataEntity.getVolume());
    }

    public List<AssetDTO> toDto(List<AssetEntity> assetEntityList) throws HistoricalDataNotFoundException {


        return assetEntityList.stream().map(assetEntity -> {
            HistoricalDataEntity historicalDataEntity = null;
            try { historicalDataEntity = assetEntity.getHistoricalDataByDate(LocalDate.now().atStartOfDay());}
            catch (HistoricalDataNotFoundException e) { throw new RuntimeException(e); }

            try {
                return new AssetDTO(assetEntity.getId(),
                            assetEntity.getTicker(),
                            assetEntity.getValueByDate(LocalDate.now().atStartOfDay()),
                            assetEntity.getDailyChange(LocalDate.now().atStartOfDay()),
                            historicalDataEntity.getOpenPrice(),
                            historicalDataEntity.getClosePrice(),
                            historicalDataEntity.getHighPrice(),
                            historicalDataEntity.getLowPrice(),
                            historicalDataEntity.getVolume());
            } catch (HistoricalDataNotFoundException e) { throw new RuntimeException(e); }
        }).collect(Collectors.toList());
    }

    public Optional<AssetEntity> toEntity(AssetDTO dto) {
        Optional<AssetEntity> assetEntity = assetService.getAssetById(dto.id());
        return Optional.ofNullable(assetEntity.orElseThrow(null));
    }

}
