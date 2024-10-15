package com.team1.investsim.repositories.transactions;

import com.team1.investsim.entities.TransactionEntity;
import com.team1.investsim.repositories.Repository;

import java.util.List;

public interface TransactionRepository extends Repository<TransactionEntity> {
    @Override
    TransactionEntity findByID(long id);

    @Override
    void save(TransactionEntity transactionEntity);

    @Override
    List<TransactionEntity> findAll();
}
