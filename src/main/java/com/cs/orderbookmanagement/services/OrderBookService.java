package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.*;
import org.hibernate.hql.internal.QueryExecutionRequestException;

public interface OrderBookService {

    public OrderDetails addOrderToOrderBook(Order order, int instrumentId);

    public String changeOrderBookStatus(int instrumentId, String command);

    public ExecutedOrderResponse addExecutionAndProcessOrder(InstrumentRequest execution, int instrumentId);
}
