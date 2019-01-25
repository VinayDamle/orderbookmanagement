package com.cs.orderbookmanagement.utils;

import com.cs.orderbookmanagement.models.Execution;
import com.cs.orderbookmanagement.models.OrderDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class OrderBookHelper {

    public List<OrderDetails> getValidOrders(List<OrderDetails> orderDetails, Execution execution) {
        List<OrderDetails> validOrderDetails = new ArrayList<>();
        Iterator<OrderDetails> iterator = orderDetails.iterator();
        while (iterator.hasNext()) {
            OrderDetails orderDetail = iterator.next();
            if (orderDetail.getOrder().getPrice() >= 0.0) {
                orderDetail.setOrderType(OrderBookConstants.LIMIT_ORDER);
            }
            if (orderDetail.getOrderType().equalsIgnoreCase(OrderBookConstants.LIMIT_ORDER)) {
                if (orderDetail.getOrder().getPrice() >= execution.getExecutionPrice()) {
                    orderDetail.setOrderStatus(OrderBookConstants.VALID);
                    validOrderDetails.add(orderDetail);
                    iterator.remove();
                }
            }
        }
        return validOrderDetails;
    }

    public List<OrderDetails> getExecutedOrderDetails(List<OrderDetails> validOrderDetails, Execution execution) {
        double totalDemandedQty = 0.0;
        for (OrderDetails validOrderDetail : validOrderDetails) {
            totalDemandedQty = totalDemandedQty + validOrderDetail.getOrder().getQuantity();
        }
        double distributionFactor = execution.getQuantity() / totalDemandedQty;
        for (OrderDetails validOrderDetail : validOrderDetails) {
            validOrderDetail.setAllocatedQuantity((int) (validOrderDetail.getOrder().getQuantity() * distributionFactor));
        }
        List<OrderDetails> executedOrderDetails = new ArrayList<>(validOrderDetails);
        return executedOrderDetails;
    }

}
