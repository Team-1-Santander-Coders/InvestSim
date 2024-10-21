package com.team1.investsim.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricalDataDTO(long id, LocalDateTime date, BigDecimal openPrice, BigDecimal closePrice, BigDecimal highPrice, BigDecimal lowPrice, long volume) {}
