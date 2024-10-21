package com.team1.investsim.controllers;


import com.team1.investsim.dtos.AssetTransactionRequestDTO;
import com.team1.investsim.dtos.PortfolioDTO;

import com.team1.investsim.dtos.TransactionDTO;
import com.team1.investsim.entities.AssetEntity;
import com.team1.investsim.entities.AssetHoldingEntity;
import com.team1.investsim.entities.TransactionEntity;
import com.team1.investsim.entities.PortfolioEntity;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.entities.types.TransactionType;
import com.team1.investsim.exceptions.AssetHoldingNotFoundException;
import com.team1.investsim.exceptions.AssetNotFoundException;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import com.team1.investsim.mappers.AssetHoldingMapper;
import com.team1.investsim.mappers.AssetMapper;
import com.team1.investsim.mappers.PortfolioMapper;
import com.team1.investsim.mappers.TransactionMapper;
import com.team1.investsim.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import javax.sound.sampled.Port;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final PortfolioService portfolioService;
    private final PortfolioMapper portfolioMapper;
    private final TransactionMapper transactionMapper;
    private final AssetService assetService;
    private final AssetHoldingService assetHoldingService;
    private final AssetHoldingMapper assetHoldingMapper;
    private final AssetMapper assetMapper;
    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public UserController(PortfolioMapper portfolioMapper, PortfolioService portfolioService, TransactionMapper transactionMapper, AssetService assetService, AssetHoldingService assetHoldingService, AssetHoldingMapper assetHoldingMapper, AssetMapper assetMapper, TransactionService transactionService, UserService userService) {
        this.portfolioService = portfolioService;
        this.portfolioMapper = portfolioMapper;
        this.transactionMapper = transactionMapper;
        this.assetService = assetService;
        this.assetHoldingService = assetHoldingService;
        this.assetHoldingMapper = assetHoldingMapper;
        this.assetMapper = assetMapper;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/portfolio")
    public ResponseEntity<?> getPortfolio() {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            Optional<PortfolioDTO> portfolioDTO = Optional.ofNullable(portfolioMapper.toDto(authenticatedUser.getPortfolio()));
            return ResponseEntity.ok(portfolioDTO.orElseThrow(Exception::new));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Portfólio inexistente: " + e.getLocalizedMessage());
        }
    }


    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions() {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            Optional<List<TransactionDTO>> transactionDTOList = Optional.ofNullable(transactionMapper.toDTO(authenticatedUser.getPortfolio().getTransactions()));
            return ResponseEntity.ok(transactionDTOList.orElseThrow(Exception::new));
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista de transações não pôde ser localizada: " + e.getLocalizedMessage());
            }
    }

    @PostMapping("/buyAsset")
    public ResponseEntity<?> buyAsset(@RequestBody AssetTransactionRequestDTO assetTransactionRequestDTO) {

        try {
            System.out.println(assetTransactionRequestDTO.assetEntityId());
            Optional<AssetEntity> assetEntity = assetService.getAssetById(assetTransactionRequestDTO.assetEntityId());
            if (assetEntity.isEmpty()) throw new AssetNotFoundException();
            if (assetTransactionRequestDTO.quantity() <= 0) throw new IllegalArgumentException("A quantidade não pode ser inferior ou igual a 0.");

            UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AssetEntity asset = assetEntity.get();

            LocalDateTime transactionDateTime = LocalDateTime.now();
            int assetQuantity = assetTransactionRequestDTO.quantity();
            double transactionPrice = asset.getValueByDate(transactionDateTime).multiply(BigDecimal.valueOf(assetQuantity)).doubleValue();

            TransactionEntity transactionEntity = new TransactionEntity(asset, transactionDateTime, assetQuantity, transactionPrice, TransactionType.BUY);
            AssetHoldingEntity assetHoldingEntity = new AssetHoldingEntity(asset, assetQuantity, transactionEntity, authenticatedUser.getPortfolio());
            PortfolioEntity portfolioEntity = authenticatedUser.getPortfolio();
            portfolioEntity.addTransaction(transactionEntity);
            portfolioEntity.addAssetHolding(assetHoldingEntity);

            transactionService.saveTransaction(transactionEntity);
            assetHoldingService.saveAssetHolding(assetHoldingEntity);
            portfolioService.savePortfolio(portfolioEntity);

            return ResponseEntity.ok(transactionEntity);

        } catch (AssetNotFoundException | IllegalArgumentException  | HistoricalDataNotFoundException e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getLocalizedMessage());
        }
    }

    @PostMapping("/sellAssets")
    public ResponseEntity<?> sellAssets(@RequestBody AssetTransactionRequestDTO assetTransactionRequestDTO) {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<AssetHoldingEntity>> assetHoldingEntity = authenticatedUser.getPortfolio().getAssetHoldingByAssetId(assetTransactionRequestDTO.assetEntityId());

        try {
            List<AssetHoldingEntity> assetHoldingList = assetHoldingEntity.get();
            if (assetTransactionRequestDTO.quantity() <= 0 ) throw new IllegalArgumentException("A quantidade não pode ser inferior ou igual a 0.");
            if (assetTransactionRequestDTO.quantity() > assetHoldingList.stream()
                    .map(AssetHoldingEntity::getQuantity)
                    .reduce(0.0, Double::sum))  throw new IllegalArgumentException("A quantidade não pode ser superior á quantidade de ações no portfolio");

            LocalDateTime transactionDateTime = LocalDateTime.now();
            int assetTransactionQuantity = assetTransactionRequestDTO.quantity();

            double transactionPrice = assetHoldingList.stream().reduce(0.0, (price, asset) -> {
                try {
                    return price + assetTransactionQuantity * asset.getValue(transactionDateTime).doubleValue();
                } catch (HistoricalDataNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }, Double::sum);

            TransactionEntity sellTransaction = sellAssetHoldings(assetHoldingList, assetTransactionRequestDTO.quantity());
            PortfolioEntity portfolioEntity = authenticatedUser.getPortfolio();
            portfolioEntity.addTransaction(sellTransaction);

            transactionService.saveTransaction(sellTransaction);
            portfolioService.savePortfolio(portfolioEntity);

            return ResponseEntity.ok(sellTransaction.getPrice());

        } catch (AssetHoldingNotFoundException | IllegalArgumentException | HistoricalDataNotFoundException e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getLocalizedMessage());
        }
    }


    private TransactionEntity sellAssetHoldings(List<AssetHoldingEntity> assetHoldingEntityList, double sellQuantity) throws AssetHoldingNotFoundException, HistoricalDataNotFoundException {
        UserEntity authenticatedUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        double transactionPrice = assetHoldingEntityList.getFirst().getAsset().getValueByDate(LocalDateTime.now()).doubleValue() * sellQuantity;

        for(AssetHoldingEntity assetHoldingEntity : assetHoldingEntityList){
            double holdingQuantity = assetHoldingEntity.getQuantity();
            double sellAmount = Math.min(holdingQuantity, sellQuantity);
            double newQuantity = holdingQuantity - sellAmount;

            assetHoldingEntity.setQuantity(newQuantity);
            assetHoldingService.saveAssetHolding(assetHoldingEntity);

            if (newQuantity == 0) userService.removeAssetHolding(authenticatedUser, assetHoldingEntity);

            sellQuantity -= sellAmount;

            if (sellQuantity == 0)  break;
        }

        if (sellQuantity > 0) {
            throw new IllegalArgumentException("Quantidade insuficiente de ativos para vender.");
        }

        return new TransactionEntity(assetHoldingEntityList.getFirst().getAsset(), LocalDateTime.now(), sellQuantity, transactionPrice, TransactionType.SELL);

    }
}
