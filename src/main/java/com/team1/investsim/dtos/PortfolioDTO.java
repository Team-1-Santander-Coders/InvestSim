package com.team1.investsim.dtos;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioDTO(long id, BigDecimal totalValue, BigDecimal dailyReturn, List<TransactionDTO> transactions, List<AssetHoldingDTO> assetHoldings) {}
