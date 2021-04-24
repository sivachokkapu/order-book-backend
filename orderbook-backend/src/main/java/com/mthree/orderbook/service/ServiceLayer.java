package com.mthree.orderbook.service;

import com.mthree.orderbook.entities.Order;
import com.mthree.orderbook.entities.OrderType;
import com.mthree.orderbook.entities.Stock;
import com.mthree.orderbook.entities.Trade;
import com.mthree.orderbook.repositories.OrderRepository;
import com.mthree.orderbook.repositories.StockRepository;
import com.mthree.orderbook.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ServiceLayer {
    @Autowired
    OrderRepository orders;

    @Autowired
    StockRepository stocks;

    @Autowired
    TradeRepository trades;

    // ORDERS

    public List<Order> getAllActiveOrders() {
        return orders.findAllActiveOrders();
    }

    public List<Order> getAllOrders() {
        return orders.findAll();
    }

    public List<Order> getAllActiveBuyOrders() {
        return orders.findActiveOrderByType(OrderType.BUY.toString());
    }

    public List<Order> getAllActiveSellOrders() {
        return orders.findActiveOrderByType(OrderType.SELL.toString());
    }

    public List<Order> getAllBuyOrders() {
        return orders.findByType(OrderType.BUY);
    }

    public List<Order> getAllSellOrders() {
        return orders.findByType(OrderType.SELL);
    }

    public List<Order> getAllOrdersByStock(Stock stock) {
        return orders.findByStock(stock);
    }

    public List<Order> getAllActiveBuyOrdersByStock(String symbol) {
        return orders.findAllActiveByTypeAndStock(OrderType.BUY.toString(), symbol);
    }

    public List<Order> getAllActiveSellOrdersByStock(String symbol) {
        return orders.findAllActiveByTypeAndStock(OrderType.SELL.toString(), symbol);
    }

    public List<Trade> addOrder(Order order) {
        orders.save(order);

        if(order.getType() == OrderType.BUY) {
            List<Order> matchingSellOrders = orders.findCheapestMatchingByTypeAndStock(OrderType.SELL.toString(), order.getStock().getSymbol(), order.getPrice());
            return matchOrder(order, matchingSellOrders, OrderType.BUY);
        } else {
            List<Order> matchingBuyOrders = orders.findMostExpensiveMatchingByTypeAndStock(OrderType.BUY.toString(), order.getStock().getSymbol(), order.getPrice());
            return matchOrder(order, matchingBuyOrders, OrderType.SELL);
        }
    }

    public boolean updateOrder(Order order) {
       if(orders.existsById(order.getOrderId())) {
           orders.save(order);
           return true;
       }
       return false;
    }

    public boolean deleteOrder(Order order) {
        if(orders.existsById(order.getOrderId())) {
            orders.delete(order);
            return true;
        }
        return false;
    }

    public boolean removeOrder(int orderId) {
        if(orders.existsById(orderId)) {
            Order removedOrder = orders.findById(orderId).get();
            removedOrder.setCurrentSize(0);
            orders.save(removedOrder);
            return true;
        }
        return false;
    }

    public Order getOrder(int orderId) {
        return orders.findById(orderId).get();
    }

    // TRADES

    public List<Trade> getAllTrades() {
        return trades.findAll();
    }

    public List<Trade> getTradesByStock(Stock stock) { return trades.findByStock(stock); }

    public Trade getTrade(int tradeId) { return trades.findById(tradeId).get(); }

    public Trade getLastTradeForStock(String symbol) { return trades.findLastTradeForStock(symbol); }

    // STOCKS

    public Stock getStock(String symbol) {
        return stocks.findBySymbol(symbol);
    }

    public List<Stock> getAllStocks() {
        return stocks.findAll();
    }

    public void addStock(Stock stock) {
        stocks.save(stock);
    }

    //

    @Transactional
    public List<Trade> matchOrder(Order newOrder, List<Order> matchingOrders, OrderType type) {
        int quantity = newOrder.getCurrentSize();
        List<Trade> matchedTrades = new ArrayList<>();

        for(Order matchingOrder : matchingOrders) {
            if(quantity > 0) {
                Trade matchedTrade = new Trade();
                matchedTrade.setStock(newOrder.getStock());

                if (quantity >= matchingOrder.getCurrentSize()) {
                    matchedTrade.setSize(matchingOrder.getCurrentSize());
                    matchedTrade.setTradePrice(matchingOrder.getPrice());

                    quantity -= matchingOrder.getCurrentSize();
                    newOrder.setCurrentSize(quantity);
                    matchingOrder.setCurrentSize(0);

                } else {
                    matchedTrade.setSize(quantity);
                    matchedTrade.setTradePrice(matchingOrder.getPrice());

                    quantity = matchingOrder.getCurrentSize() - quantity;
                    newOrder.setCurrentSize(0);
                    matchingOrder.setCurrentSize(quantity);

                    quantity = 0;
                }

                updateOrder(newOrder);
                updateOrder(matchingOrder);

                if(type == OrderType.BUY) {
                    matchedTrade.setBuyOrder(newOrder);
                    matchedTrade.setSellOrder(matchingOrder);
                } else {
                    matchedTrade.setBuyOrder(matchingOrder);
                    matchedTrade.setSellOrder(newOrder);
                }

                trades.save(matchedTrade);
                matchedTrades.add(matchedTrade);
            } else {
                break;
            }
        }
        return matchedTrades;
    }
}
