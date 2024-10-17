package com.team1.investsim.dtos;

import java.math.BigDecimal;

public record AssetDTO(long id, String ticker, BigDecimal currentPrice, BigDecimal dailyChange) {}
