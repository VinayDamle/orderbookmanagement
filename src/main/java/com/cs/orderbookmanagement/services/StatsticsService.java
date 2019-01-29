package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.OrderState;
import com.cs.orderbookmanagement.models.OrderStatstics;

import java.util.List;

public interface StatsticsService {

    List<OrderDetail> getAllOrders();

    OrderState getOrderStateByOrderId(int orderId);

    OrderDetail getOrderDetailsByOrderId(int orderId);

    List<OrderDetail> getOrderDetailsByInstId(int instrumentId);

    OrderStatstics getOrderStatstics(boolean fetchValidInvalidRecords);

}
