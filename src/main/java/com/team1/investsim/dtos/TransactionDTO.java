package com.team1.investsim.dtos;

import com.team1.investsim.entities.types.TransactionType;

import java.time.LocalDateTime;

public record TransactionDTO(long id, String assetTicker, double quantity, double price, LocalDateTime date, TransactionType type) {}
