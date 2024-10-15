package com.team1.investsim.entities;

import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class PortfolioEntity implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<AssetHoldingEntity> assetHoldings;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions;

    public BigDecimal getReturn(LocalDateTime start, LocalDateTime end) {
        BigDecimal startValue = getTotalValue(start);
        BigDecimal endValue = getTotalValue(end);
        return startValue.subtract(endValue);
    }

    public BigDecimal getTotalValue(LocalDateTime date) throws RuntimeException {
        return assetHoldings.stream()
                .map(assetHolding -> {
                    try {
                        return assetHolding.getValue(date);
                    } catch (HistoricalDataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public List<AssetHoldingEntity> getAssetHoldings() {
        return assetHoldings;
    }

    public void setAssetHoldings(List<AssetHoldingEntity> assetHoldings) {
        this.assetHoldings = assetHoldings;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(TransactionEntity transactionEntity) {
        if (transactions.isEmpty()) setTransactions(new ArrayList<>());
        this.transactions.add(transactionEntity);
    }

    public void addAssetHolding(AssetHoldingEntity assetHoldingEntity) {
        if (assetHoldings.isEmpty()) setAssetHoldings(new ArrayList<>());
        this.assetHoldings.add(assetHoldingEntity);
    }
}
