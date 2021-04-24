/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.repositories;

import com.mthree.orderbook.entities.Stock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Samuel Bristow
 */
@SpringBootTest
public class StockRepositoryTest {
    
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
    public void testFindBySymbol() {
        
        Stock appleStock = new Stock();
        appleStock.setSymbol("APPL");
        appleStock.setName("Apple inc.");
        appleStock.setExchange("NASDAQ");
        stocks.save(appleStock);
        
        Stock retrievedStock = stocks.findBySymbol(appleStock.getSymbol());
        
        assertEquals(appleStock, retrievedStock);
    }
 
}
