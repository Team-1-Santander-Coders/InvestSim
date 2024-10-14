package com.team1.investsim.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assets")
public class AssetEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoricalDataEntity> historicalData;

    public double getReturn(LocalDateTime start, LocalDateTime end) {
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

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public List<HistoricalDataEntity> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<HistoricalDataEntity> historicalData) {
        this.historicalData = historicalData;
    }
}
