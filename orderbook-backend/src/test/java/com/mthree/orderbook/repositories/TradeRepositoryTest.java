/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.repositories;

import com.mthree.orderbook.entities.Order;
import com.mthree.orderbook.entities.OrderType;
import com.mthree.orderbook.entities.Stock;
import com.mthree.orderbook.entities.Trade;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Samuel Bristow
 */
@SpringBootTest
public class TradeRepositoryTest {
    
    
    @Autowired
    TradeRepository trades;
    
    @Autowired
    StockRepository stocks;
    
    @Autowired
    OrderRepository orders;
    
    @BeforeEach
    public void setUp() {
        trades.deleteAll();
        stocks.deleteAll();
        orders.deleteAll();
    }
    
    
    @Test
    public void testFindByStock() {
        
        Stock appleStock = new Stock();
        appleStock.setSymbol("APPL");
        appleStock.setName("Apple inc.");
        appleStock.setExchange("NASDAQ");
        stocks.save(appleStock);
        
        Stock avivaStock = new Stock();
        avivaStock.setSymbol("AV");
        avivaStock.setName("Aviva plc.");
        avivaStock.setExchange("LON");
        stocks.save(avivaStock);
        
        Order buyOrder1 = new Order();
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setPrice(new BigDecimal(1.20));
        buyOrder1.setInitialSize(20);
        buyOrder1.setCurrentSize(20);
        buyOrder1.setStock(appleStock);
        buyOrder1.setOrderTimestamp(LocalDateTime.now());
        buyOrder1.setUserId(1);
        orders.save(buyOrder1);
        
        Order sellOrder1 = new Order();
        sellOrder1.setType(OrderType.SELL);
        sellOrder1.setPrice(new BigDecimal(1.20));
        sellOrder1.setInitialSize(20);
        sellOrder1.setCurrentSize(20);
        sellOrder1.setStock(appleStock);
        sellOrder1.setOrderTimestamp(LocalDateTime.now());
        sellOrder1.setUserId(2);
        orders.save(sellOrder1);
        
        Trade trade1 = new Trade();
        trade1.setSellOrder(sellOrder1);
        trade1.setBuyOrder(buyOrder1);
        trade1.setSize(20);
        trade1.setTradePrice(new BigDecimal(1.20));
        trade1.setTradeTimestamp(LocalDateTime.now());
        trade1.setStock(appleStock);
        trades.save(trade1);
        
        Order buyOrder2 = new Order();
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setPrice(new BigDecimal(1.50));
        buyOrder2.setInitialSize(50);
        buyOrder2.setCurrentSize(50);
        buyOrder2.setStock(appleStock);
        buyOrder2.setOrderTimestamp(LocalDateTime.now());
        buyOrder2.setUserId(3);
        orders.save(buyOrder2);
        
        Order sellOrder2 = new Order();
        sellOrder2.setType(OrderType.SELL);
        sellOrder2.setPrice(new BigDecimal(1.50));
        sellOrder2.setInitialSize(50);
        sellOrder2.setCurrentSize(50);
        sellOrder2.setStock(appleStock);
        sellOrder2.setOrderTimestamp(LocalDateTime.now());
        sellOrder2.setUserId(4);
        orders.save(sellOrder2);
        
        Trade trade2 = new Trade();
        trade2.setSellOrder(sellOrder2);
        trade2.setBuyOrder(buyOrder2);
        trade2.setSize(50);
        trade2.setTradePrice(new BigDecimal(1.50));
        trade2.setTradeTimestamp(LocalDateTime.now());
        trade2.setStock(appleStock);
        trades.save(trade2);
        
        Order buyOrder3 = new Order();
        buyOrder3.setType(OrderType.BUY);
        buyOrder3.setPrice(new BigDecimal(1.50));
        buyOrder3.setInitialSize(50);
        buyOrder3.setCurrentSize(50);
        buyOrder3.setStock(avivaStock);
        buyOrder3.setOrderTimestamp(LocalDateTime.now());
        buyOrder3.setUserId(5);
        orders.save(buyOrder3);
        
        Order sellOrder3 = new Order();
        sellOrder3.setType(OrderType.SELL);
        sellOrder3.setPrice(new BigDecimal(1.50));
        sellOrder3.setInitialSize(50);
        sellOrder3.setCurrentSize(50);
        sellOrder3.setStock(avivaStock);
        sellOrder3.setOrderTimestamp(LocalDateTime.now());
        sellOrder3.setUserId(6);
        orders.save(sellOrder3);
        
        Trade trade3 = new Trade();
        trade3.setSellOrder(sellOrder3);
        trade3.setBuyOrder(buyOrder3);
        trade3.setSize(50);
        trade3.setTradePrice(new BigDecimal(1.50));
        trade3.setTradeTimestamp(LocalDateTime.now());
        trade3.setStock(avivaStock);
        trades.save(trade3);
        
        List<Trade> retrievedAppleTrades = trades.findByStock(appleStock);
        List<Trade> retrievedAvivaTrades = trades.findByStock(avivaStock);
        
        List<Trade> testAppleTrades = new ArrayList<>();
        testAppleTrades.add(trades.findById(trade1.getTradeId()).orElse(null));
        testAppleTrades.add(trades.findById(trade2.getTradeId()).orElse(null));
        
        List<Trade> testAvivaTrades = new ArrayList<>();
        testAvivaTrades.add(trades.findById(trade3.getTradeId()).orElse(null));
        
        assertEquals(testAppleTrades,retrievedAppleTrades);
        assertEquals(testAvivaTrades,retrievedAvivaTrades);
    }
    
    @Test
    public void findByOrder(){
    
        Stock appleStock = new Stock();
        appleStock.setSymbol("APPL");
        appleStock.setName("Apple inc.");
        appleStock.setExchange("NASDAQ");
        stocks.save(appleStock);
        
        Order buyOrder = new Order();
        buyOrder.setType(OrderType.BUY);
        buyOrder.setPrice(new BigDecimal(1.20));
        buyOrder.setInitialSize(20);
        buyOrder.setCurrentSize(20);
        buyOrder.setStock(appleStock);
        buyOrder.setOrderTimestamp(LocalDateTime.now());
        buyOrder.setUserId(1);
        orders.save(buyOrder);
        
        Order sellOrder = new Order();
        sellOrder.setType(OrderType.SELL);
        sellOrder.setPrice(new BigDecimal(1.20));
        sellOrder.setInitialSize(20);
        sellOrder.setCurrentSize(20);
        sellOrder.setStock(appleStock);
        sellOrder.setOrderTimestamp(LocalDateTime.now());
        sellOrder.setUserId(2);
        orders.save(sellOrder);
        
        Trade trade = new Trade();
        trade.setSellOrder(sellOrder);
        trade.setBuyOrder(buyOrder);
        trade.setSize(20);
        trade.setTradePrice(new BigDecimal(1.20));
        trade.setTradeTimestamp(LocalDateTime.now());
        trade.setStock(appleStock);
        trades.save(trade);
        
        List<Trade> testTrade = new ArrayList<>();
        testTrade.add(trades.findById(trade.getTradeId()).orElse(null));
        
        List<Trade> retrievedTradeBuy = trades.findByOrder(buyOrder.getOrderId());
        List<Trade> retrievedTradeSell = trades.findByOrder(sellOrder.getOrderId());
        
        assertEquals(testTrade, retrievedTradeBuy);
        assertEquals(testTrade, retrievedTradeSell);
        
        
        
        
        
    }
    

}
