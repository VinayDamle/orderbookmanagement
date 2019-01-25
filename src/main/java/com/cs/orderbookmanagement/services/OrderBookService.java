package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.*;

public interface OrderBookService {

    public OrderDetails addOrderToOrderBook(Order order, int instrumentId);

    public String changeOrderBookStatus(int instrumentId, String command);

    public ExecutedOrderResponse addExecutionAndProcessOrder(ExecutionRequest execution, int instrumentId);
}
