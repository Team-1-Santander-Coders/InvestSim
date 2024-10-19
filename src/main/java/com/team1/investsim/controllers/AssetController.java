package com.team1.investsim.controllers;

import com.team1.investsim.dtos.AssetDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.mappers.AssetMapper;
import com.team1.investsim.services.AssetService;
import com.team1.investsim.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class AssetController {
    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetMapper assetMapper;

    private static final String DEFAULT_DATE_FORMAT = DateUtil.ISO8601_DATE_PATTERN;

    @GetMapping("/assets")
    public ResponseEntity<?> getAssetsList() {
        try {
            List<AssetDTO> assetList = assetMapper.toDto(assetService.getAllAssets());
            return ResponseEntity.ok(assetList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getLocalizedMessage());
        }
    }

    @GetMapping("/asset/{id}")
    public ResponseEntity<?> getAssetData(
            @PathVariable("id") String id,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "date", required = false) String date) {
        Optional<AssetEntity> asset = assetService.getAssetById(Long.parseLong(id));

        if (asset.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID não localizado");
        AssetEntity assetEntity = asset.get();
        try {
            LocalDateTime today = DateUtil.getStartOfDay(LocalDateTime.now());
            if (date != null && !date.isEmpty()) {
                return ResponseEntity.ok(assetEntity.getHistoricalDataByDate(DateUtil.stringToDate(date, DEFAULT_DATE_FORMAT)));
            }
            if ((startDate != null && !startDate.isEmpty()) || (endDate != null && !endDate.isEmpty())) {
                if (startDate == null || startDate.isEmpty()) {
                    return ResponseEntity.ok(assetEntity.getHistoricalDataByPeriod(today.minusDays(30), DateUtil.stringToDate(endDate, DEFAULT_DATE_FORMAT)));
                }
                else if (endDate == null || endDate.isEmpty()) {
                    return ResponseEntity.ok(assetEntity.getHistoricalDataByPeriod(DateUtil.stringToDate(startDate, DEFAULT_DATE_FORMAT), today));
                }
                else {
                    return ResponseEntity.ok(assetEntity.getHistoricalDataByPeriod(DateUtil.stringToDate(startDate, DEFAULT_DATE_FORMAT), DateUtil.stringToDate(endDate, DEFAULT_DATE_FORMAT)));
                }
            }
            return ResponseEntity.ok(assetEntity.getHistoricalDataByPeriod(today.minusDays(30), today));
        } catch (HistoricalDataNotFoundException | IllegalDateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getLocalizedMessage());
        }

    }
}
