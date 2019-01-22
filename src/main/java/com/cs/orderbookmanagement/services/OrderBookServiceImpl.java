package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderBookServiceImpl implements OrderBookService {

    @Override
    public String changeOrderBookStatus(int instrumentId, String command) {
        if (instrumentId == 10) {
            return OrderBookService.INSTRUMENT_ID_NOT_FOUND;
        } else {
            if (command != null) {
                return OrderBookService.OPEN.equalsIgnoreCase(command) ? OrderBookService.OPENED : OrderBookService.CLOSED;
            }
        }
        return null;
    }

    @Override
    public OrderDetails addOrder(Order order, int instrumentId) {
        OrderDetails orderDetails = null;
        if (instrumentId == 10) {
            orderDetails = new OrderDetails(0, 0, null, null, 0, null,
                    new Error(OrderBookService.OBMS_INVL_001, OrderBookService.INSTRUMENT_ID_NOT_FOUND));
        } else {
            orderDetails = new OrderDetails(1, order.getInstrumentId(),
                    new Order(order.getQuantity(), order.getEntryDate(), order.getInstrumentId(), order.getPrice()),
                    null, 0, order.getPrice() != 0 ? OrderBookService.LIMIT_ORDER : OrderBookService.MARKET_ORDER, null);
        }
        return orderDetails;
    }

    @Override
    public ExecutedOrderResponse addExecutionAndProcessOrder(ExecutionRequest executionRequest, int instrumentId) {
        ExecutedOrderResponse executedOrderResponse = null;
        if (instrumentId == 10) {
            executedOrderResponse = new ExecutedOrderResponse(
                    new Error(OrderBookService.OBMS_INVL_001, OrderBookService.INSTRUMENT_ID_NOT_FOUND), null);
        } else {
            Order order = new Order(10, "10-10-2019", instrumentId, 100);
            OrderDetails orderDetails = new OrderDetails(1, instrumentId,
                    order, "Valid", executionRequest.getQuantity(),
                    executionRequest.getExecutionPrice() != 0 ? OrderBookService.LIMIT_ORDER : OrderBookService.MARKET_ORDER, null);
            List<OrderDetails> orderDetailsList = new ArrayList<>();
            orderDetailsList.add(orderDetails);
            OrderBookHelper.filterValidOrders(orderDetailsList, executionRequest);
            OrderBookHelper.distributeExecutionLinearly(orderDetailsList, executionRequest);
            executedOrderResponse = new ExecutedOrderResponse(null, orderDetails);
        }
        return executedOrderResponse;
    }



}
