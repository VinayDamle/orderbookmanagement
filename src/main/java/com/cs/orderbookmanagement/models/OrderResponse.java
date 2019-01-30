package com.cs.orderbookmanagement.models;

import com.cs.orderbookmanagement.entities.OrderBook;
import com.cs.orderbookmanagement.entities.OrderDao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties
public class OrderResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private int orderDetailsId;

    private OrderDao order;

    private OrderType orderType;

    private OrderBook orderBook;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int allocatedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double executionPrice;

    public OrderResponse(int instrumentId, OrderDao order, String orderStatus, int allocatedQuantity, OrderType orderType, double executionPrice) {
        this.order = order;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.executionPrice = executionPrice;
        this.allocatedQuantity = allocatedQuantity;
    }

    public OrderResponse(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderDetailsId=" + orderDetailsId +
                ", orderType=" + orderType +
                ", order=" + order +
                ", error=" + error +
                ", orderStatus='" + orderStatus + '\'' +
                ", executionPrice=" + executionPrice +
                ", allocatedQuantity=" + allocatedQuantity +
                '}';
    }
}
