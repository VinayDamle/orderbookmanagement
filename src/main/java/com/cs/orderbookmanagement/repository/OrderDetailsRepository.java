package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {


    List<OrderDetail> findAllOrderDetailsByOrderInstrumentId(Long instrumentId);

}
