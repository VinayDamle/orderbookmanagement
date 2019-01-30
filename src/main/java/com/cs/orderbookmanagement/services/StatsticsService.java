package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.OrderResponse;
import com.cs.orderbookmanagement.models.OrderState;
import com.cs.orderbookmanagement.models.OrderStatstics;

import java.util.List;

public interface StatsticsService {

    List<OrderResponse> getAllOrders();

    OrderState getOrderStateByOrderId(Long orderId);

    OrderResponse getOrderDetailsByOrderId(Long orderId);

    List<OrderResponse> getOrderDetailsByInstId(Long instrumentId);

    OrderStatstics getOrderStatstics(Boolean fetchValidInvalidRecords);

}
