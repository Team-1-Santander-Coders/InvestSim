package com.team1.investsim.dtos;

import java.math.BigDecimal;

public record AssetHoldingDTO(long id, AssetDTO asset, double quantity, TransactionDTO transcation) {
    public BigDecimal getCurrentValue(){
        return asset.currentPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
