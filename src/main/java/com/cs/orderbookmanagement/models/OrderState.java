package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class OrderState {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private int orderId;

    private double orderPrice;

    private String validityStatus;

    private double executionPrice;

    private int executionQuantity;

    public OrderState() {
    }

    public OrderState(int orderId, double orderPrice, int executionQuantity, double executionPrice, String validityStatus) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.validityStatus = validityStatus;
        this.executionPrice = executionPrice;
        this.executionQuantity = executionQuantity;
    }

    public OrderState(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OrderState{" +
                "error=" + error +
                ", orderId=" + orderId +
                ", orderPrice=" + orderPrice +
                ", validityStatus='" + validityStatus + '\'' +
                ", executionPrice=" + executionPrice +
                ", executionQuantity=" + executionQuantity +
                '}';
    }
}
