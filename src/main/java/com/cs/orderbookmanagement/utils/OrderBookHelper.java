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
                } else {
                    orderDetail.setOrderStatus(OrderBookConstants.INVALID);
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
        return getExecuteOrderByDistributionFactor(validOrderDetails, execution.getQuantity() / totalDemandedQty, execution);
    }

    private List<OrderDetails> getExecuteOrderByDistributionFactor(List<OrderDetails> validOrderDetails, double distributionFactor, Execution execution) {
        validOrderDetails.forEach(validOrderDetail -> {
            validOrderDetail.setAllocatedQuantity((int) (validOrderDetail.getOrder().getQuantity() * distributionFactor));
            if (validOrderDetail.getAllocatedQuantity() > 0) {
                //====================================================================================================//
                /*
                 ASSUMPTION - 1
                 The execution price for allocated quantity would be the price it was quoted while adding order
                  */
                //validOrderDetail.setExecutionPrice(validOrderDetail.getOrder().getPrice());
                //====================================================================================================//
                /*
                ASSUMPTION - 2
                The execution price for allocated quantity would be calculated as below
                price for 1 unit -> execution.getExecutionPrice() / execution.getQuantity() = X
                Execution price for all the allocated quantity -> allocated quantity * X = Y
                validOrderDetail.setExecutionPrice(Y);
                */
                validOrderDetail.setExecutionPrice(validOrderDetail.getAllocatedQuantity() * (execution.getExecutionPrice() / execution.getQuantity()));
                //====================================================================================================//
                /*
                ASSUMPTION - 3
                The execution price for allocated quantity would be the execution price
                used while executing order irrespective of quantity

                program
                validOrderDetail.setExecutionPrice(execution.getExecutionPrice());
                 */
            }
        });
        return new ArrayList<>(validOrderDetails);
    }

}
