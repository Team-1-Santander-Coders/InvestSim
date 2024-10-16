package com.team1.investsim.entities;

import com.team1.investsim.exceptions.IllegalDateException;
import com.team1.investsim.utils.DateUtil;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "historical_data")
public class HistoricalDataEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal openPrice;

    @Column(nullable = false)
    private BigDecimal closePrice;

    @Column(nullable = false)
    private BigDecimal highPrice;

    @Column(nullable = false)
    private BigDecimal lowPrice;

    @Column(nullable = false)
    private long volume;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetEntity asset;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
    }

    public void setAll(AssetEntity asset, String date, String openPrice, String closePrice, String highPrice, String lowPrice, String volume) throws IllegalDateException {
        this.date = DateUtil.stringToDate(date, "yyyy-MM-dd");
        this.openPrice = BigDecimal.valueOf(Double.valueOf(openPrice));
        this.closePrice = BigDecimal.valueOf(Double.valueOf(closePrice));
        this.highPrice = BigDecimal.valueOf(Double.valueOf(highPrice));
        this.lowPrice = BigDecimal.valueOf(Double.valueOf(lowPrice));
        this.volume = Double.valueOf(volume).longValue();
        this.asset = asset;
    }

    public String toString(){
        return "id: "+ this.id + " - date: "+this.getDate() +" - ticker: " + this.getAsset().getTicker() + " - open: " + this.getOpenPrice() + " - close: " + this.getClosePrice();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalDataEntity that)) return false;
        return Objects.equals(date, that.date) && Objects.equals(asset, that.asset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, asset);
    }
}