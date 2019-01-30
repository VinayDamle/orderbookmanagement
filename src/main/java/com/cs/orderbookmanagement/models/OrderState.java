package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties
public class OrderState {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private Long orderId;

    private BigDecimal orderPrice;

    private String validityStatus;

    private BigDecimal executionPrice;

    private Integer executionQuantity;

    public OrderState() {
    }

    public OrderState(Long orderId, BigDecimal orderPrice, Integer executionQuantity, BigDecimal executionPrice, String validityStatus) {
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
