/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Samuel Bristow
 */
@Entity(name = "trades")
public class Trade {
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int tradeId;
    
    @ManyToOne
    @JoinColumn(name = "sell_id", nullable = false)
    private Order sellOrder;
    
    @ManyToOne
    @JoinColumn(name = "buy_id", nullable = false)
    private Order buyOrder;
    
    @Column (nullable = false)
    private int size;
    
    @Column (nullable = false)
    private BigDecimal tradePrice;
    
    @Column
    private LocalDateTime tradeTimestamp;
    
    @ManyToOne
    @JoinColumn(name = "symbol", nullable = false)
    private Stock stock;
    
    private BigDecimal getTotalAmount(){
        return tradePrice.multiply(new BigDecimal(size));
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
    
    public LocalDateTime getTradeTimestamp() {
        return tradeTimestamp;
    }

    public void setTradeTimestamp(LocalDateTime tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.tradeId;
        hash = 89 * hash + Objects.hashCode(this.sellOrder);
        hash = 89 * hash + Objects.hashCode(this.buyOrder);
        hash = 89 * hash + this.size;
        hash = 89 * hash + Objects.hashCode(this.tradePrice);
        hash = 89 * hash + Objects.hashCode(this.tradeTimestamp);
        hash = 89 * hash + Objects.hashCode(this.stock);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Trade other = (Trade) obj;
        if (this.tradeId != other.tradeId) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.sellOrder, other.sellOrder)) {
            return false;
        }
        if (!Objects.equals(this.buyOrder, other.buyOrder)) {
            return false;
        }
        if (!Objects.equals(this.tradePrice, other.tradePrice)) {
            return false;
        }
        if (!Objects.equals(this.tradeTimestamp, other.tradeTimestamp)) {
            return false;
        }
        if (!Objects.equals(this.stock, other.stock)) {
            return false;
        }
        return true;
    }

    
    
    
}
