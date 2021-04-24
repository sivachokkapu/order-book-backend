package com.mthree.orderbook.repositories;

import com.mthree.orderbook.entities.Order;
import com.mthree.orderbook.entities.OrderType;
import com.mthree.orderbook.entities.Stock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    TradeRepository tradeRepository;

    @BeforeEach
    void setUp() {
        Stock amzn = new Stock();
        amzn.setSymbol("AMZN");
        amzn.setName("Amazon.com Inc");
        amzn.setExchange("NASDAQ");

        Stock appl = new Stock();
        appl.setSymbol("APPL");
        appl.setName("Apple Inc");
        appl.setExchange("NASDAQ");

        stockRepository.save(amzn);
        stockRepository.save(appl);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Test
    void findByType() {
        // Arrange
        Stock amzn = stockRepository.findBySymbol("AMZN");
        Stock appl = stockRepository.findBySymbol("APPL");

        Order sellOrder1 = new Order();
        sellOrder1.setType(OrderType.SELL);
        sellOrder1.setPrice(new BigDecimal("45.78"));
        sellOrder1.setInitialSize(200);
        sellOrder1.setCurrentSize(200);
        sellOrder1.setStock(amzn);
        sellOrder1.setOrderTimestamp(LocalDateTime.now());
        sellOrder1.setUserId(2);

        Order sellOrder2 = new Order();
        sellOrder2.setType(OrderType.SELL);
        sellOrder2.setPrice(new BigDecimal("95.78"));
        sellOrder2.setInitialSize(200);
        sellOrder2.setCurrentSize(0);
        sellOrder2.setStock(appl);
        sellOrder2.setOrderTimestamp(LocalDateTime.now());
        sellOrder2.setUserId(2);

        Order buyOrder1 = new Order();
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setPrice(new BigDecimal("45.78"));
        buyOrder1.setInitialSize(50);
        buyOrder1.setCurrentSize(30);
        buyOrder1.setStock(amzn);
        buyOrder1.setOrderTimestamp(LocalDateTime.now());
        buyOrder1.setUserId(3);

        Order buyOrder2 = new Order();
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setPrice(new BigDecimal("95.78"));
        buyOrder2.setInitialSize(60);
        buyOrder2.setCurrentSize(0);
        buyOrder2.setStock(appl);
        buyOrder2.setOrderTimestamp(LocalDateTime.now());
        buyOrder2.setUserId(3);

        orderRepository.save(sellOrder1);
        orderRepository.save(sellOrder2);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        List<Order> testSellOrders = new ArrayList<>();
        testSellOrders.add(orderRepository.findById(sellOrder1.getOrderId()).orElse(null));
        testSellOrders.add(orderRepository.findById(sellOrder2.getOrderId()).orElse(null));

        List<Order> testBuyOrders = new ArrayList<>();
        testBuyOrders.add(orderRepository.findById(buyOrder1.getOrderId()).orElse(null));
        testBuyOrders.add(orderRepository.findById(buyOrder2.getOrderId()).orElse(null));

        // Act
        List<Order> retrievedSellOrders = orderRepository.findByType(OrderType.SELL);
        List<Order> retrievedBuyOrders = orderRepository.findByType(OrderType.BUY);

        // Arrange
        assertEquals(testSellOrders, retrievedSellOrders);
        assertEquals(testBuyOrders, retrievedBuyOrders);
    }

    @Test
    void findByStock() {
        // Arrange
        Stock amzn = stockRepository.findBySymbol("AMZN");
        Stock appl = stockRepository.findBySymbol("APPL");

        Order sellOrder1 = new Order();
        sellOrder1.setType(OrderType.SELL);
        sellOrder1.setPrice(new BigDecimal("45.78"));
        sellOrder1.setInitialSize(200);
        sellOrder1.setCurrentSize(200);
        sellOrder1.setStock(amzn);
        sellOrder1.setOrderTimestamp(LocalDateTime.now());
        sellOrder1.setUserId(2);

        Order sellOrder2 = new Order();
        sellOrder2.setType(OrderType.SELL);
        sellOrder2.setPrice(new BigDecimal("95.78"));
        sellOrder2.setInitialSize(200);
        sellOrder2.setCurrentSize(0);
        sellOrder2.setStock(appl);
        sellOrder2.setOrderTimestamp(LocalDateTime.now());
        sellOrder2.setUserId(2);

        Order buyOrder1 = new Order();
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setPrice(new BigDecimal("45.78"));
        buyOrder1.setInitialSize(50);
        buyOrder1.setCurrentSize(30);
        buyOrder1.setStock(amzn);
        buyOrder1.setOrderTimestamp(LocalDateTime.now());
        buyOrder1.setUserId(3);

        Order buyOrder2 = new Order();
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setPrice(new BigDecimal("95.78"));
        buyOrder2.setInitialSize(60);
        buyOrder2.setCurrentSize(0);
        buyOrder2.setStock(appl);
        buyOrder2.setOrderTimestamp(LocalDateTime.now());
        buyOrder2.setUserId(3);

        orderRepository.save(sellOrder1);
        orderRepository.save(sellOrder2);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        List<Order> testAmznStockOrders = new ArrayList<>();
        testAmznStockOrders.add(orderRepository.findById(sellOrder1.getOrderId()).orElse(null));
        testAmznStockOrders.add(orderRepository.findById(buyOrder1.getOrderId()).orElse(null));

        List<Order> testApplStockOrders = new ArrayList<>();
        testApplStockOrders.add(orderRepository.findById(sellOrder2.getOrderId()).orElse(null));
        testApplStockOrders.add(orderRepository.findById(buyOrder2.getOrderId()).orElse(null));

        // Act
        List<Order> retrievedAmznStockOrders = orderRepository.findByStock(amzn);
        List<Order> retrievedApplStockOrders = orderRepository.findByStock(appl);

        // Assert
        assertEquals(testAmznStockOrders, retrievedAmznStockOrders);
        assertEquals(testApplStockOrders, retrievedApplStockOrders);
    }

    @Test
    void findByTypeAndStock() {
        // Arrange
        Stock amzn = stockRepository.findBySymbol("AMZN");
        Stock appl = stockRepository.findBySymbol("APPL");

        Order sellOrder1 = new Order();
        sellOrder1.setType(OrderType.SELL);
        sellOrder1.setPrice(new BigDecimal("45.78"));
        sellOrder1.setInitialSize(200);
        sellOrder1.setCurrentSize(200);
        sellOrder1.setStock(amzn);
        sellOrder1.setOrderTimestamp(LocalDateTime.now());
        sellOrder1.setUserId(2);

        Order sellOrder2 = new Order();
        sellOrder2.setType(OrderType.SELL);
        sellOrder2.setPrice(new BigDecimal("95.78"));
        sellOrder2.setInitialSize(200);
        sellOrder2.setCurrentSize(0);
        sellOrder2.setStock(appl);
        sellOrder2.setOrderTimestamp(LocalDateTime.now());
        sellOrder2.setUserId(2);

        Order buyOrder1 = new Order();
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setPrice(new BigDecimal("45.78"));
        buyOrder1.setInitialSize(50);
        buyOrder1.setCurrentSize(30);
        buyOrder1.setStock(amzn);
        buyOrder1.setOrderTimestamp(LocalDateTime.now());
        buyOrder1.setUserId(3);

        Order buyOrder2 = new Order();
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setPrice(new BigDecimal("95.78"));
        buyOrder2.setInitialSize(60);
        buyOrder2.setCurrentSize(0);
        buyOrder2.setStock(appl);
        buyOrder2.setOrderTimestamp(LocalDateTime.now());
        buyOrder2.setUserId(3);

        orderRepository.save(sellOrder1);
        orderRepository.save(sellOrder2);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        List<Order> testAmznStockSellOrders = new ArrayList<>();
        testAmznStockSellOrders.add(orderRepository.findById(sellOrder1.getOrderId()).orElse(null));

        List<Order> testAmznStockBuyOrders = new ArrayList<>();
        testAmznStockBuyOrders.add(orderRepository.findById(buyOrder1.getOrderId()).orElse(null));

        List<Order> testApplStockSellOrders = new ArrayList<>();
        testApplStockSellOrders.add(orderRepository.findById(sellOrder2.getOrderId()).orElse(null));

        List<Order> testApplStockBuyOrders = new ArrayList<>();
        testApplStockBuyOrders.add(orderRepository.findById(buyOrder2.getOrderId()).orElse(null));

        // Act
        List<Order> retrievedAmznStockSellOrders = orderRepository.findByTypeAndStock(OrderType.SELL, amzn);
        List<Order> retrievedAmznStockBuyOrders = orderRepository.findByTypeAndStock(OrderType.BUY, amzn);
        List<Order> retrievedApplStockSellOrders = orderRepository.findByTypeAndStock(OrderType.SELL, appl);
        List<Order> retrievedApplStockBuyOrders = orderRepository.findByTypeAndStock(OrderType.BUY, appl);

        // Assert
        assertEquals(testAmznStockSellOrders, retrievedAmznStockSellOrders);
        assertEquals(testAmznStockBuyOrders, retrievedAmznStockBuyOrders);
        assertEquals(testApplStockSellOrders, retrievedApplStockSellOrders);
        assertEquals(testApplStockBuyOrders, retrievedApplStockBuyOrders);
    }

    @Test
    void findAllActiveOrders() {
        // Arrange
        Stock stock = stockRepository.findBySymbol("AMZN");
        Stock stock2 = stockRepository.findBySymbol("APPL");

        Order order = new Order();
        order.setType(OrderType.SELL);
        order.setPrice(new BigDecimal("45.78"));
        order.setInitialSize(200);
        order.setCurrentSize(200);
        order.setStock(stock);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setUserId(2);

        Order order2 = new Order();
        order2.setType(OrderType.SELL);
        order2.setPrice(new BigDecimal("95.78"));
        order2.setInitialSize(200);
        order2.setCurrentSize(0);
        order2.setStock(stock2);
        order2.setOrderTimestamp(LocalDateTime.now());
        order2.setUserId(2);

        orderRepository.save(order);
        orderRepository.save(order2);
        List<Order> testActiveOrders = new ArrayList<>();
        testActiveOrders.add(orderRepository.findById(order.getOrderId()).orElse(null));

        // Act
        List<Order> retrievedActiveOrders = orderRepository.findAllActiveOrders();

        // Assert
        assertEquals(testActiveOrders, retrievedActiveOrders);
    }

    @Test
    void findActiveOrderByType() {
        // Arrange
        Stock stock = stockRepository.findBySymbol("AMZN");
        Stock stock2 = stockRepository.findBySymbol("APPL");

        Order sellOrder1 = new Order();
        sellOrder1.setType(OrderType.SELL);
        sellOrder1.setPrice(new BigDecimal("45.78"));
        sellOrder1.setInitialSize(200);
        sellOrder1.setCurrentSize(200);
        sellOrder1.setStock(stock);
        sellOrder1.setOrderTimestamp(LocalDateTime.now());
        sellOrder1.setUserId(2);

        Order sellOrder2 = new Order();
        sellOrder2.setType(OrderType.SELL);
        sellOrder2.setPrice(new BigDecimal("95.78"));
        sellOrder2.setInitialSize(200);
        sellOrder2.setCurrentSize(0);
        sellOrder2.setStock(stock2);
        sellOrder2.setOrderTimestamp(LocalDateTime.now());
        sellOrder2.setUserId(2);

        Order buyOrder1 = new Order();
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setPrice(new BigDecimal("45.78"));
        buyOrder1.setInitialSize(50);
        buyOrder1.setCurrentSize(30);
        buyOrder1.setStock(stock);
        buyOrder1.setOrderTimestamp(LocalDateTime.now());
        buyOrder1.setUserId(3);

        Order buyOrder2 = new Order();
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setPrice(new BigDecimal("95.78"));
        buyOrder2.setInitialSize(60);
        buyOrder2.setCurrentSize(0);
        buyOrder2.setStock(stock2);
        buyOrder2.setOrderTimestamp(LocalDateTime.now());
        buyOrder2.setUserId(3);

        orderRepository.save(sellOrder1);
        orderRepository.save(sellOrder2);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        List<Order> testActiveSellOrders = new ArrayList<>();
        testActiveSellOrders.add(orderRepository.findById(sellOrder1.getOrderId()).orElse(null));

        List<Order> testActiveBuyOrders = new ArrayList<>();
        testActiveBuyOrders.add(orderRepository.findById(buyOrder1.getOrderId()).orElse(null));

        // Act
        List<Order> retrievedActiveSellOrders = orderRepository.findActiveOrderByType(OrderType.SELL.toString());
        List<Order> retrievedActiveBuyOrders = orderRepository.findActiveOrderByType(OrderType.BUY.toString());

        // Assert
        assertEquals(testActiveSellOrders, retrievedActiveSellOrders);
        assertEquals(testActiveBuyOrders, retrievedActiveBuyOrders);
    }

    @Test
    void findActiveByTypeAndStock() {
        // Arrange
        Stock amzn = stockRepository.findBySymbol("AMZN");
        Stock appl = stockRepository.findBySymbol("APPL");

        Order sellOrder1 = new Order();
        sellOrder1.setType(OrderType.SELL);
        sellOrder1.setPrice(new BigDecimal("45.78"));
        sellOrder1.setInitialSize(200);
        sellOrder1.setCurrentSize(200);
        sellOrder1.setStock(amzn);
        sellOrder1.setOrderTimestamp(LocalDateTime.now());
        sellOrder1.setUserId(2);

        Order sellOrder2 = new Order();
        sellOrder2.setType(OrderType.SELL);
        sellOrder2.setPrice(new BigDecimal("95.78"));
        sellOrder2.setInitialSize(200);
        sellOrder2.setCurrentSize(0);
        sellOrder2.setStock(amzn);
        sellOrder2.setOrderTimestamp(LocalDateTime.now());
        sellOrder2.setUserId(2);

        Order buyOrder1 = new Order();
        buyOrder1.setType(OrderType.BUY);
        buyOrder1.setPrice(new BigDecimal("45.78"));
        buyOrder1.setInitialSize(50);
        buyOrder1.setCurrentSize(30);
        buyOrder1.setStock(amzn);
        buyOrder1.setOrderTimestamp(LocalDateTime.now());
        buyOrder1.setUserId(3);

        Order buyOrder2 = new Order();
        buyOrder2.setType(OrderType.BUY);
        buyOrder2.setPrice(new BigDecimal("95.78"));
        buyOrder2.setInitialSize(60);
        buyOrder2.setCurrentSize(0);
        buyOrder2.setStock(amzn);
        buyOrder2.setOrderTimestamp(LocalDateTime.now());
        buyOrder2.setUserId(3);

        orderRepository.save(sellOrder1);
        orderRepository.save(sellOrder2);
        orderRepository.save(buyOrder1);
        orderRepository.save(buyOrder2);

        List<Order> testAmznStockSellOrders = new ArrayList<>();
        testAmznStockSellOrders.add(orderRepository.findById(sellOrder1.getOrderId()).orElse(null));

        List<Order> testAmznStockBuyOrders = new ArrayList<>();
        testAmznStockBuyOrders.add(orderRepository.findById(buyOrder1.getOrderId()).orElse(null));

        // Act
        List<Order> retrievedAmznStockSellOrders = orderRepository.findAllActiveByTypeAndStock(OrderType.SELL.toString(), amzn.getSymbol());
        List<Order> retrievedAmznStockBuyOrders = orderRepository.findAllActiveByTypeAndStock(OrderType.BUY.toString(), amzn.getSymbol());

        // Assert
        assertEquals(testAmznStockSellOrders, retrievedAmznStockSellOrders);
        assertEquals(testAmznStockBuyOrders, retrievedAmznStockBuyOrders);
    }
}