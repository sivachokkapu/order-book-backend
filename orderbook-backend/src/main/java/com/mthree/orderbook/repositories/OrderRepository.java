/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.repositories;

import com.mthree.orderbook.entities.Order;
import com.mthree.orderbook.entities.OrderType;

import java.math.BigDecimal;
import java.util.List;

import com.mthree.orderbook.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Samuel Bristow
 */
@Repository
public interface OrderRepository extends JpaRepository <Order, Integer>{
    

    @Query
    List<Order> findByType(OrderType type);
    
    @Query
    List<Order> findByStock(Stock stock);
    
    @Query
    List<Order> findByTypeAndStock(OrderType type, Stock stock);
    
    @Query(value = "SELECT o.* FROM orders o "
            + "WHERE o.current_size > 0", nativeQuery = true)
    List<Order> findAllActiveOrders();

    @Query(value = "SELECT o.* FROM orders o "
            + "WHERE o.current_size > 0 "
            + "AND o.type = :type", nativeQuery = true)
    List<Order> findActiveOrderByType(@Param("type")String type);

    @Query(value = "SELECT o.* FROM orders o "
            + "WHERE o.current_size > 0 "
            + "AND o.type = :type "
            + "AND o.symbol = :symbol", nativeQuery = true)
    List<Order> findAllActiveByTypeAndStock(@Param("type")String type, @Param("symbol") String symbol);

    @Query(value = "SELECT o.* FROM orders o "
            + "WHERE o.current_size > 0 "
            + "AND o.type = :type "
            + "AND o.symbol = :symbol "
            + "AND o.price <= :price "
            + "ORDER BY price ASC", nativeQuery = true)
    List<Order> findCheapestMatchingByTypeAndStock(@Param("type")String type, @Param("symbol") String symbol, @Param("price") BigDecimal price);

    @Query(value = "SELECT o.* FROM orders o "
            + "WHERE o.current_size > 0 "
            + "AND o.type = :type "
            + "AND o.symbol = :symbol "
            + "AND o.price >= :price "
            + "ORDER BY price DESC", nativeQuery = true)
    List<Order> findMostExpensiveMatchingByTypeAndStock(@Param("type")String type, @Param("symbol") String symbol, @Param("price") BigDecimal price);
    
}
