package com.cs.orderbookmanagement.repository;

import com.cs.orderbookmanagement.entities.OrderDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDaoRepository extends JpaRepository<OrderDao, Integer> {


}
