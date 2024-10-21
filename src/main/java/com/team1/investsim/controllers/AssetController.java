package com.team1.investsim.controllers;

import com.team1.investsim.dtos.AssetDTO;
import com.team1.investsim.dtos.AssetSimpleDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.HistoricalDataEntity;
import com.team1.investsim.exceptions.AssetNotFoundException;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.mappers.AssetMapper;
import com.team1.investsim.mappers.HistoricalDataMapper;
import com.team1.investsim.services.AssetService;
import com.team1.investsim.services.HistoricalDataService;
import com.team1.investsim.utils.CSVProcessor;
import com.team1.investsim.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class AssetController {
    @Autowired
    private AssetService assetService;

    @Autowired
    private HistoricalDataService historicalDataService;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private HistoricalDataMapper historicalDataMapper;

    private static final String DEFAULT_DATE_FORMAT = DateUtil.ISO8601_DATE_PATTERN;

    @GetMapping("/assets")
    public ResponseEntity<?> getAssetsList() {
        try {
            List<AssetSimpleDTO> assetList = assetService.getAllAssetsAsDTO();
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

        if (asset.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID não localizado");
        }

        AssetEntity assetEntity = asset.get();
        try {
            LocalDateTime today = DateUtil.getStartOfDay(LocalDateTime.now());
            List<HistoricalDataEntity> historicalDataEntities;

            if (date != null && !date.isEmpty()) {
                historicalDataEntities = assetService.getHistoricalDataByPeriod(assetEntity.getId(),
                        DateUtil.stringToDate(date, DEFAULT_DATE_FORMAT),
                        DateUtil.stringToDate(date, DEFAULT_DATE_FORMAT));
                return ResponseEntity.ok(historicalDataMapper.toDTO(historicalDataService.saveAllHistoricalData(historicalDataEntities)));
            }

            if ((startDate != null && !startDate.isEmpty()) || (endDate != null && !endDate.isEmpty())) {
                if (startDate == null || startDate.isEmpty()) {
                    historicalDataEntities = assetService.getHistoricalDataByPeriod(assetEntity.getId(),
                            today.minusDays(15),
                            DateUtil.stringToDate(endDate, DEFAULT_DATE_FORMAT));
                } else if (endDate == null || endDate.isEmpty()) {
                    historicalDataEntities = assetService.getHistoricalDataByPeriod(assetEntity.getId(),
                            DateUtil.stringToDate(startDate, DEFAULT_DATE_FORMAT),
                            today);
                } else {
                    historicalDataEntities = assetService.getHistoricalDataByPeriod(assetEntity.getId(),
                            DateUtil.stringToDate(startDate, DEFAULT_DATE_FORMAT),
                            DateUtil.stringToDate(endDate, DEFAULT_DATE_FORMAT));
                }
                return ResponseEntity.ok(historicalDataMapper.toDTO(historicalDataService.saveAllHistoricalData(historicalDataEntities)));
            }

            historicalDataEntities = assetService.getHistoricalDataByPeriod(assetEntity.getId(),
                    today.minusDays(15),
                    today);
            return ResponseEntity.ok(historicalDataMapper.toDTO(historicalDataService.saveAllHistoricalData(historicalDataEntities)));
        } catch (HistoricalDataNotFoundException | IllegalDateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getLocalizedMessage());
        }
    }

    @GetMapping("/assetname/{ticker}")
    public ResponseEntity<?> getNameByTicker(@PathVariable ("ticker") String ticker){

        String name = "";
        Optional<List<String[]>> companiesList = CSVProcessor.processCSV("src/main/resources/data/empresas.csv");
        try {
            if(companiesList.isPresent()) {
                for (String[] row : companiesList.get()) {
                    if (row[0].equalsIgnoreCase(ticker)) {
                        name = row[1];
                        break;
                    }
                }
            }
            if (name.isEmpty()) throw new AssetNotFoundException();
            return ResponseEntity.ok(name);

        } catch (AssetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getLocalizedMessage());
        }
    }
}
