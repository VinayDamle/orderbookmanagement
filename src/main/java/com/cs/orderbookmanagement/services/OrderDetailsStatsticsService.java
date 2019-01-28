package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.OrderDetails;
import com.cs.orderbookmanagement.models.OrderState;
import com.cs.orderbookmanagement.models.OrderStatstics;

import java.util.List;

public interface OrderDetailsStatsticsService {

    List<OrderDetails> getAllOrders();

    OrderState getOrderStateByOrderId(int orderId);

    OrderDetails getOrderDetailsByOrderId(int orderId);

    List<OrderDetails> getOrderDetailsByInstId(int instrumentId);

    OrderStatstics getOrderStatstics(boolean fetchValidInvalidRecords);

}
