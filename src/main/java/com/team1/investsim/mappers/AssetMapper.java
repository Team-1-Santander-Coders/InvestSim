package com.team1.investsim.mappers;

import com.team1.investsim.dtos.AssetDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.services.AssetService;

import com.team1.investsim.services.HistoricalDataService;
import com.team1.investsim.utils.ThrowingFunctionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AssetMapper {
    @Autowired
    private AssetService assetService;

    @Autowired
    private HistoricalDataService historicalDataService;

    public AssetDTO toDto(AssetEntity assetEntity) throws HistoricalDataNotFoundException {
        return createAssetDTO(assetEntity);
    }

    public List<AssetDTO> toDto(List<AssetEntity> assetEntityList) throws Exception {
        return assetEntityList.stream()
                .map(ThrowingFunctionWrapper.wrap(this::createAssetDTO))
                .collect(Collectors.toList());
    }

    private AssetDTO createAssetDTO(AssetEntity assetEntity) throws HistoricalDataNotFoundException {
        HistoricalDataEntity historicalDataEntity = assetEntity.getHistoricalDataByDate(LocalDate.now().atStartOfDay());

        return new AssetDTO(
                assetEntity.getId(),
                assetEntity.getTicker(),
                assetEntity.getValueByDate(LocalDateTime.now()),
                assetEntity.getDailyChange(LocalDateTime.now()),
                historicalDataEntity.getOpenPrice(),
                historicalDataEntity.getVolume()
        );
    }

    public Optional<AssetEntity> toEntity(AssetDTO dto) {
        Optional<AssetEntity> assetEntity = assetService.getAssetById(dto.id());
        return Optional.ofNullable(assetEntity.orElseThrow(null));
    }

}
