package com.team1.investsim.dtos;

import java.math.BigDecimal;

public record AssetHoldingDTO(long id, AssetDTO asset, double quantity, TranscationDTO transcation) {
    public BigDecimal getCurrentValue(){
        return BigDecimal.valueOf(asset.currentPrice() * this.quantity);
    }
}
