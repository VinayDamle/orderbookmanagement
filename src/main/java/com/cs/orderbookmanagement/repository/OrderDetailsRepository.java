package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {


    public List<OrderDetails> findAllOrderDetailsByOrderInstrumentId(int instrumentId);

}
