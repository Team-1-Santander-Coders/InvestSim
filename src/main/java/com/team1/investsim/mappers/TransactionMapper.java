package com.team1.investsim.mappers;

import com.team1.investsim.dtos.TransactionDTO;
import com.team1.investsim.entities.TransactionEntity;
import com.team1.investsim.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    @Autowired
    private TransactionService transactionService;

    public TransactionDTO toDTO(TransactionEntity transaction) {
        return new TransactionDTO ( transaction.getId(),
                                    transaction.getAsset().getTicker(),
                                    transaction.getQuantity(),
                                    transaction.getPrice(),
                                    transaction.getDate(),
                                    transaction.getType());
    }

    public List<TransactionDTO> toDTO(List<TransactionEntity> transactions) {
        return transactions.stream().map(transaction -> {
            return new TransactionDTO ( transaction.getId(),
                    transaction.getAsset().getTicker(),
                    transaction.getQuantity(),
                    transaction.getPrice(),
                    transaction.getDate(),
                    transaction.getType());
        }).collect(Collectors.toList());
    }

    public TransactionEntity toEntity(TransactionDTO transactionDTO) {
        return transactionService.getTransactionById(transactionDTO.id()).get();
    }
}
