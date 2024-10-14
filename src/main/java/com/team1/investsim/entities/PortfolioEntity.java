package com.team1.investsim.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    public double getReturn(LocalDateTime start, LocalDateTime end) {
        return 0.0;
    }

    public double getTotalValue(LocalDateTime date) {
        return 0.0;
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
}
