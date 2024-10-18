package com.team1.investsim.services;

import com.team1.investsim.entities.PortfolioEntity;
import com.team1.investsim.entities.TransactionEntity;
import com.team1.investsim.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) { this.transactionRepository = transactionRepository; }

    public void saveTransaction(TransactionEntity transactionEntity) { transactionRepository.saveAndFlush(transactionEntity); }

    public List<TransactionEntity> getAllTransactions() { return transactionRepository.findAll(); }

    public Optional<TransactionEntity> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public long countTransactions() {
        return transactionRepository.count();
    }
}
