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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Samuel Bristow
 */
@Entity (name = "orders")
public class Order {
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id 
    private int orderId;
    
    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private OrderType type;
    
    @Column (nullable = false)
    private BigDecimal price;
    
    @Column (nullable = false)
    private int initialSize;
    
    @Column (nullable = false)
    private int currentSize;
    
    @ManyToOne
    @JoinColumn(name = "symbol", nullable = false)
    private Stock stock;
    
    @Column
    private LocalDateTime orderTimestamp;
    
    @Column (nullable = false)
    private int userId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(LocalDateTime orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.orderId;
        hash = 19 * hash + Objects.hashCode(this.type);
        hash = 19 * hash + Objects.hashCode(this.price);
        hash = 19 * hash + this.initialSize;
        hash = 19 * hash + this.currentSize;
        hash = 19 * hash + Objects.hashCode(this.stock);
        hash = 19 * hash + Objects.hashCode(this.orderTimestamp);
        hash = 19 * hash + this.userId;
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
        final Order other = (Order) obj;
        if (this.orderId != other.orderId) {
            return false;
        }
        if (this.initialSize != other.initialSize) {
            return false;
        }
        if (this.currentSize != other.currentSize) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.stock, other.stock)) {
            return false;
        }
        if (!Objects.equals(this.orderTimestamp, other.orderTimestamp)) {
            return false;
        }
        return true;
    }

    
    
    
}
