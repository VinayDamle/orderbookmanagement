package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {


}
