package com.team1.investsim.dtos;

import com.team1.investsim.Utils.DateUtil;
import com.team1.investsim.exceptions.IllegalDateException;

import java.time.LocalDateTime;

public record AssetHoldingDTO(long id, AssetDTO asset, double quantity, TranscationDTO transcation) {
    public double getValue(LocalDateTime date) throws IllegalDateException {
        return 0;
    }
}
