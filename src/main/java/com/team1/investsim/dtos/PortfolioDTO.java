package com.team1.investsim.dtos;

import java.util.List;

public record PortfolioDTO(long id, double totalValue, double dailyReturn, List<TranscationDTO> transactions, AssetHoldingDTO assetHoldings) {}
