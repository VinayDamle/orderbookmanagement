package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.*;

public interface OrderBookService {

    Long openOrderBook();

    String closeOrderBook(Long instrumentId);

    //OrderDetail addOrder(OrderRequest order, int instrumentId);

    OrderResponse addOrder(OrderRequest order, Long instrumentId);

    ExecuteOrderResponse addExecutionAndProcessOrder(ExecuteOrderRequest execution, Long instrumentId);
}
