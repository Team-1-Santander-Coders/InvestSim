package com.team1.investsim.entities;

import com.team1.investsim.utils.DateUtil;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "assets")
public class AssetEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HistoricalDataEntity> historicalData;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getReturn(LocalDateTime start, LocalDateTime end) throws HistoricalDataNotFoundException {
        BigDecimal startValue = this.getHistoricalDataByDate(start).getClosePrice();
        BigDecimal endValue = this.getHistoricalDataByDate(end).getClosePrice();
        return startValue.subtract(endValue);
    }

    public BigDecimal getValueByDate(LocalDateTime date) throws HistoricalDataNotFoundException {
        return getHistoricalDataByDate(date).getClosePrice();
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

    public void addHistoricalData(HistoricalDataEntity historicalData) {
        this.historicalData.add(historicalData);
    }

    public HistoricalDataEntity getHistoricalDataByDate(LocalDateTime date) throws HistoricalDataNotFoundException {
        return historicalData.stream()
                .filter(historicalData -> historicalData.getDate().isEqual(date))
                .findFirst()
                .orElseThrow(() -> new HistoricalDataNotFoundException("Dado histórico não encontrado para " + DateUtil.dateToString(date)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetEntity that)) return false;
        return id == that.id || Objects.equals(ticker, that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker);
    }
}
