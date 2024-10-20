package com.team1.investsim.controllers;

import com.team1.investsim.dtos.PortfolioDTO;

import com.team1.investsim.dtos.TransactionDTO;
import com.team1.investsim.entities.UserEntity;
import com.team1.investsim.mappers.PortfolioMapper;
import com.team1.investsim.mappers.TransactionMapper;
import com.team1.investsim.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final PortfolioService portfolioService;
    private final PortfolioMapper portfolioMapper;
    private final TransactionMapper transactionMapper;

    @Autowired
    public UserController(PortfolioMapper portfolioMapper, PortfolioService portfolioService, TransactionMapper transactionMapper) {
        this.portfolioService = portfolioService;
        this.portfolioMapper = portfolioMapper;
        this.transactionMapper = transactionMapper;
    }


    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Sucesso!");
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


}
