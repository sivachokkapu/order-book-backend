/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.repositories;

import com.mthree.orderbook.entities.Stock;
import com.mthree.orderbook.entities.Trade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Samuel Bristow
 */
@Repository

public interface TradeRepository extends JpaRepository <Trade, Integer>{

    @Query
    List<Trade> findByStock(Stock stock);
    
    @Query (value = "SELECT t.* from trades t" +
            " WHERE sell_id = :orderId OR buy_id = :orderId", nativeQuery = true)
    List<Trade> findByOrder(@Param("orderId") int orderId);

    @Query(value = "SELECT t.* from trades t" +
            " WHERE symbol = :symbol ORDER BY trade_timestamp DESC LIMIT 1", nativeQuery = true)
    Trade findLastTradeForStock(@Param("symbol") String symbol);
}
