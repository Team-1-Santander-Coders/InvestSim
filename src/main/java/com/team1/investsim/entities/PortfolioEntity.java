package com.team1.investsim.entities;

import com.team1.investsim.exceptions.AssetHoldingNotFoundException;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "portfolios")
public class PortfolioEntity implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AssetHoldingEntity> assetHoldings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public Optional<List<AssetHoldingEntity>> getAssetHoldingByAssetId(long assetHoldingidAssetId) {
        return Optional.of(getAssetHoldings().stream().filter(assetHoldingEntity -> assetHoldingidAssetId == assetHoldingEntity.getAsset().getId()).collect(Collectors.toList()));
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
        this.transactions.add(transactionEntity);
    }

    public void addAssetHolding(AssetHoldingEntity assetHoldingEntity) {
        this.assetHoldings.add(assetHoldingEntity);
    }

    public void removeAssetHolding(AssetHoldingEntity assetHoldingEntity) throws AssetHoldingNotFoundException {
        if (!assetHoldings.contains(assetHoldingEntity)) throw new AssetHoldingNotFoundException("Aporte de ações não encontrado neste portfólio");
        else assetHoldings.remove(assetHoldingEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortfolioEntity that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
