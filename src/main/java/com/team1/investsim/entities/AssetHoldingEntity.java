package com.team1.investsim.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "asset_holdings")
public class AssetHoldingEntity implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetEntity asset;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionEntity buyTransaction;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private PortfolioEntity portfolio;

    public double getValue(LocalDateTime date) {
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

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public TransactionEntity getBuyTransaction() {
        return buyTransaction;
    }

    public void setBuyTransaction(TransactionEntity buyTransaction) {
        this.buyTransaction = buyTransaction;
    }
}
