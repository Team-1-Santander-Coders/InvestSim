package com.team1.investsim.mappers;

import com.team1.investsim.dtos.AssetDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.services.AssetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;


@Component
public class AssetMapper {
    @Autowired
    private AssetService assetService;

    public AssetDTO toDto(AssetEntity assetEntity) throws HistoricalDataNotFoundException {
        return new AssetDTO(assetEntity.getId(),
                            assetEntity.getTicker(),
                            assetEntity.getValueByDate(LocalDate.now().atStartOfDay()),
                            assetEntity.getDailyChange(LocalDate.now().atStartOfDay()));
}

    public Optional<AssetEntity> toEntity(AssetDTO dto) {
        Optional<AssetEntity> assetEntity = assetService.getAssetById(dto.id());
        return Optional.ofNullable(assetEntity.orElse(null));
    }
}
