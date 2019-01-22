package com.cs.orderbookmanagement.utils;

import com.cs.orderbookmanagement.models.ExecutionRequest;
import com.cs.orderbookmanagement.models.OrderDetails;
import com.cs.orderbookmanagement.services.OrderBookService;

import java.util.ArrayList;
import java.util.List;

public class OrderBookHelper {

    public static void filterValidOrders(List<OrderDetails> orderDetails, ExecutionRequest execution) {
        List<OrderDetails> validOrderDetails = new ArrayList<>();
        for (OrderDetails orderDetail : orderDetails) {
            if (orderDetail.getOrder().getPrice() >= 0.0) {
                orderDetail.setOrderType(OrderBookService.LIMIT_ORDER);
            }
            if (orderDetail.getOrderType().equalsIgnoreCase(OrderBookService.LIMIT_ORDER) &&
                    orderDetail.getOrder().getPrice() >= execution.getExecutionPrice()) {
                orderDetail.setOrderStatus(OrderBookService.VALID);
                validOrderDetails.add(orderDetail);
            }
        }
    }

    public static void distributeExecutionLinearly(List<OrderDetails> validOrderDetails, ExecutionRequest execution) {
        double totalDemandedQty = 0.0;
        for (OrderDetails validOrderDetail : validOrderDetails) {
            totalDemandedQty = totalDemandedQty + validOrderDetail.getOrder().getQuantity();
        }
        double distributionFactor = execution.getQuantity() / totalDemandedQty;
        for (OrderDetails validOrderDetail : validOrderDetails) {
            validOrderDetail.setAllocatedQuantity((int) (validOrderDetail.getOrder().getQuantity() * distributionFactor));
        }
    }

}
