/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.controller;

import com.mthree.orderbook.entities.Order;
import com.mthree.orderbook.entities.OrderType;
import com.mthree.orderbook.entities.Stock;
import com.mthree.orderbook.entities.Trade;
import com.mthree.orderbook.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Samuel Bristow
 */
@RestController
@CrossOrigin
@RequestMapping("/orderbook")
public class Controller {
    @Autowired
    ServiceLayer service;
    
    @GetMapping("/stocks")
    public List<Stock> getAllStocks(){
        return service.getAllStocks();
    }
    
    @GetMapping("/trades") 
    public ResponseEntity<TradesPageResponse> getAllTrades() {
        List<Trade> trades = service.getAllTrades();
        List<String> stockSymbols = service.getAllStocks().stream().map( (item) -> item.getSymbol()).collect(Collectors.toList());
        return ResponseEntity.ok(new TradesPageResponse(stockSymbols,null,trades,null));
    }

    @GetMapping("/trades/{symbol}")
    public ResponseEntity<TradesPageResponse> getTradesByStock(@PathVariable String symbol){
        Stock stock = service.getStock(symbol);
        if(stock == null){
            return new ResponseEntity("Stock " + symbol + " does not exist.", HttpStatus.NOT_FOUND);
        }
        else{
            List<Trade> trades = service.getTradesByStock(stock);
            List<String> stockSymbols = service.getAllStocks().stream().map( (item) -> item.getSymbol()).collect(Collectors.toList());
            Trade lastMatch = service.getLastTradeForStock(symbol);
            return ResponseEntity.ok(new TradesPageResponse(stockSymbols,stock,trades,lastMatch));
        }
    }
    
    @GetMapping("/orders")
    public ResponseEntity<OrdersPageResponse> getAllActiveOrders(){
        List<Order> buyOrders = service.getAllActiveBuyOrders();
        List<Order> sellOrders = service.getAllActiveSellOrders();
        List<String> stockSymbols = service.getAllStocks().stream().map( (item) -> item.getSymbol()).collect(Collectors.toList());
        return ResponseEntity.ok(new OrdersPageResponse(stockSymbols,null,buyOrders,sellOrders,null));
    }

    @GetMapping("/orders/buy")
    public List<Order> getAllActiveBuyOrders(){
        return service.getAllActiveBuyOrders();
    }

    @GetMapping("/orders/sell")
    public List<Order> getAllActiveSellOrders(){
        return service.getAllActiveSellOrders();
    }

    @GetMapping("/orders/{symbol}")
    public ResponseEntity<OrdersPageResponse> getOrdersByStock(@PathVariable String symbol){
        Stock stock = service.getStock(symbol);
        if(stock == null){
            return new ResponseEntity("Stock " + symbol + " does not exist.", HttpStatus.NOT_FOUND);
        }
        else{
            List<Order> buyOrders = service.getAllActiveBuyOrdersByStock( symbol );
            List<Order> sellOrders = service.getAllActiveSellOrdersByStock( symbol );
            List<String> stockSymbols = service.getAllStocks().stream().map( (item) -> item.getSymbol()).collect(Collectors.toList());
            Trade lastMatch = service.getLastTradeForStock(symbol);
            return ResponseEntity.ok(new OrdersPageResponse(stockSymbols,stock,buyOrders,sellOrders,lastMatch));
        }
    }

    @GetMapping("/orders/{symbol}/sell")
    public List<Order> getSellOrdersByStock(@PathVariable String symbol){
        return service.getAllActiveSellOrdersByStock( symbol );
    }
    
    @PostMapping("/addorder")
    public List<Trade> addOrder(@RequestBody Order order){
        return service.addOrder(order);
    }
    
    @PostMapping("/addstock")
    public void addStock(@RequestBody Stock stock){
        service.addStock(stock);
    }
    
    @GetMapping("/removeorder/{orderId}")
    public ResponseEntity removeOrder(@PathVariable int orderId){
        if (!service.removeOrder(orderId)) {
            return new ResponseEntity("Order: " + orderId + " does not exist.", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(null);
    }
    
    @PostMapping("/changeorder")
    public ResponseEntity changeOrder(@RequestBody Order order){
        if (order == null) {
            return new ResponseEntity("Empty order1 input.", HttpStatus.BAD_REQUEST);
        } else if (order.getType() != OrderType.BUY && order.getType() != OrderType.SELL) {
            return new ResponseEntity("Empty order2 input.", HttpStatus.BAD_REQUEST);
        } else if (order.getOrderId() < 0) {
            return new ResponseEntity("Empty order3 input.", HttpStatus.BAD_REQUEST);
        } else if (order.getCurrentSize() < 0) {
            return new ResponseEntity("Order quantity cannot be negative", HttpStatus.BAD_REQUEST);
        } else if (order.getInitialSize() < 0) {
            return new ResponseEntity("Order quantity cannot be negative", HttpStatus.BAD_REQUEST);
        } else if (order.getPrice() == null) {
            return new ResponseEntity("No price associated with order", HttpStatus.BAD_REQUEST);
        } else if (order.getStock() == null) {
            return new ResponseEntity("No stock associated with order", HttpStatus.BAD_REQUEST);
        } else if (!service.updateOrder(order)) {
            return new ResponseEntity("Order: " + order.getOrderId() + " does not exist.", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(null);
    }

    private static class OrdersPageResponse{
        public List<String> stockSymbols;
        public Stock stock;
        public List<Order> buyOrders;
        public List<Order> sellOrders;
        public Trade lastMatch;

        public OrdersPageResponse(List<String> stockSymbols, Stock stock, List<Order> buyOrders, List<Order> sellOrders, Trade lastMatch) {
            this.stockSymbols = stockSymbols;
            this.stock = stock;
            this.buyOrders = buyOrders;
            this.sellOrders = sellOrders;
            this.lastMatch = lastMatch;
        }
    }

    private static class TradesPageResponse{
        public List<String> stockSymbols;
        public Stock stock;
        public List<Trade> trades;
        public Trade lastMatch;

        public TradesPageResponse(List<String> stockSymbols, Stock stock, List<Trade> trades, Trade lastMatch) {
            this.stockSymbols = stockSymbols;
            this.stock = stock;
            this.trades = trades;
            this.lastMatch = lastMatch;
        }
    }
}
