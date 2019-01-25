package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.*;

public interface OrderBookService {

    OrderDetails addOrder(Order order, int instrumentId);

    String changeOrderBookStatus(int instrumentId, String command);

    ExecutedOrderResponse addExecutionAndProcessOrder(ExecutionRequest execution, int instrumentId);
}
