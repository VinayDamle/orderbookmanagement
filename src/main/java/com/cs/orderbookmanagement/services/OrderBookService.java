package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.*;

public interface OrderBookService {

    Integer openOrderBook();

    String closeOrderBook(int instrumentId);

    //OrderDetail addOrder(OrderRequest order, int instrumentId);

    OrderResponse addOrder(OrderRequest order, int instrumentId);

    ExecuteOrderResponse addExecutionAndProcessOrder(ExecuteOrderRequest execution, int instrumentId);
}
