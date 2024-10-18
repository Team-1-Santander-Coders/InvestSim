package com.team1.investsim.mappers;

import com.team1.investsim.dtos.AssetHoldingDTO;
import com.team1.investsim.entities.AssetHoldingEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.services.AssetHoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetHoldingMapper {
    @Autowired
    private AssetHoldingService assetHoldingService;
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    public AssetHoldingDTO toDto(AssetHoldingEntity assetHolding) throws HistoricalDataNotFoundException {
        return new AssetHoldingDTO( assetHolding.getId(),
                                    assetMapper.toDto(assetHolding.getAsset()),
                                    assetHolding.getQuantity(),
                                    transactionMapper.toDTO(assetHolding.getBuyTransaction()));
    }


    public List<AssetHoldingDTO> toDto(List<AssetHoldingEntity> assetHoldingEntityList) throws HistoricalDataNotFoundException {
        return assetHoldingEntityList.stream().map(assetHoldingEntity -> {
            try {
                return new AssetHoldingDTO( assetHoldingEntity.getId(),
                                            assetMapper.toDto(assetHoldingEntity.getAsset()),
                                            assetHoldingEntity.getQuantity(),
                                            transactionMapper.toDTO(assetHoldingEntity.getBuyTransaction()));
            } catch (HistoricalDataNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public AssetHoldingEntity toEntity(AssetHoldingDTO assetHoldingDTO){
        return assetHoldingService.getAssetHoldingById(assetHoldingDTO.id()).get();
    }
}
