package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.*;

public interface OrderBookService {

    //OrderDetail addOrder(OrderRequest order, int instrumentId);

    OrderResponse addOrder(OrderRequest order, int instrumentId);

    String changeOrderBookStatus(int instrumentId, String command);

    ExecuteOrderResponse addExecutionAndProcessOrder(ExecuteOrderRequest execution, int instrumentId);
}
