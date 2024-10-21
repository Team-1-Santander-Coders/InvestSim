package com.team1.investsim.mappers;


import com.team1.investsim.dtos.AssetHoldingDTO;
import com.team1.investsim.dtos.PortfolioDTO;
import com.team1.investsim.dtos.TransactionDTO;
import com.team1.investsim.entities.PortfolioEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.services.AssetHoldingService;
import com.team1.investsim.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PortfolioMapper {
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private AssetHoldingMapper assetHoldingMapper;
    @Autowired
    private AssetHoldingService assetHoldingService;

    public PortfolioDTO toDto(PortfolioEntity portfolioEntity) throws HistoricalDataNotFoundException {
        List<TransactionDTO> transactionDTOList = transactionMapper.toDTO(portfolioEntity.getTransactions());

        List<AssetHoldingDTO> assetHoldingDTOList = assetHoldingMapper.toDto(portfolioEntity.getAssetHoldings());

        return new PortfolioDTO(portfolioEntity.getId(),
                                portfolioEntity.getTotalValue(LocalDate.now().atStartOfDay()),
                                portfolioEntity.getReturn(LocalDate.now().atStartOfDay(), LocalDate.now().minusDays(1).atStartOfDay()),
                                transactionDTOList,
                                assetHoldingDTOList);
    }

    public PortfolioEntity toEntity(PortfolioDTO portfolioDTO){
        return portfolioService.getPortfolioById(portfolioDTO.id()).get();
    }
}
