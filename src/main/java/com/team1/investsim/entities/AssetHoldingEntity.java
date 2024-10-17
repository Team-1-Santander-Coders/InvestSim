package com.team1.investsim.entities;

import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public BigDecimal getValue(LocalDateTime date) throws HistoricalDataNotFoundException {
        return asset.getValueByDate(date).multiply(BigDecimal.valueOf(quantity));
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

    public void setPortfolio(PortfolioEntity portfolio) { this.portfolio = portfolio;  }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetHoldingEntity that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
