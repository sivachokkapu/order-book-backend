/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.orderbook.repositories;

import com.mthree.orderbook.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Samuel Bristow
 */
@Repository
public interface StockRepository extends JpaRepository <Stock, String>{
    @Query
    Stock findBySymbol(String symbol);
}
