package com.team1.investsim.entities;

import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.repositories.HistoricalDataRepository;
import com.team1.investsim.utils.DataAssetPredictionUtil;
import com.team1.investsim.utils.DateUtil;
import com.team1.investsim.exceptions.HistoricalDataNotFoundException;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public AssetEntity(){}

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

    public BigDecimal getDailyChange(LocalDateTime today) throws HistoricalDataNotFoundException {
        return getValueByDate(today).subtract(getValueByDate(today.minusDays(1)));
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

    public List<HistoricalDataEntity> getHistoricalDataByPeriod(LocalDateTime start, LocalDateTime end) throws IllegalDateException, HistoricalDataNotFoundException {
        List<LocalDateTime> daysBetween = DateUtil.getDaysBetweenDates(start, end);
        List<HistoricalDataEntity> historicalDataEntities = new ArrayList<>();

        for (LocalDateTime day : daysBetween) {
            historicalDataEntities.add(getHistoricalDataByDate(day));
        }

        return historicalDataEntities;
    }

    public HistoricalDataEntity getHistoricalDataByDate(LocalDateTime date) throws HistoricalDataNotFoundException {
        LocalDateTime startOfDay = DateUtil.getStartOfDay(date);
        HistoricalDataEntity historicalDataByDate;

        try {
            historicalDataByDate = historicalData.stream()
                    .filter(historicalData -> historicalData.getDate().isEqual(startOfDay))
                    .findFirst()
                    .orElseThrow(() -> new HistoricalDataNotFoundException("Dado histórico não encontrado para " + DateUtil.dateToString(startOfDay)));
        } catch (HistoricalDataNotFoundException e) {
            try {
                historicalDataByDate = DataAssetPredictionUtil.predictAssetHistoricalData(this, startOfDay);
            } catch (Exception ex) {
                throw new HistoricalDataNotFoundException(ex.getMessage());
            }
        }

        return historicalDataByDate;
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