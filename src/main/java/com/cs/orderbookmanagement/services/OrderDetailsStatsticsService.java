package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.OrderDetails;

import java.util.List;

public interface OrderDetailsStatsticsService {

    public List<OrderDetails> getAllOrders();

    public OrderDetails getOrderDetailsByOrderId(int orderId);

    public List<OrderDetails> getOrderDetailsByInstId(int instrumentId);


}
