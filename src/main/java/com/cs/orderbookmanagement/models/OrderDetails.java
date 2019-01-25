package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@JsonIgnoreProperties
public class OrderDetails {

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    @Id
    @GeneratedValue
    private int orderId;

    @Embedded
    private OrderDao order;

    private String orderType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int allocatedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double executionPrice;

    public OrderDetails() {
    }

    public OrderDetails(int orderId, int instrumentId, OrderDao order, String orderStatus, int allocatedQuantity, String orderType, double executionPrice) {
        this.order = order;
        this.orderId = orderId;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.executionPrice = executionPrice;
        this.allocatedQuantity = allocatedQuantity;
    }

    public OrderDetails(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId=" + orderId +
                ", orderType=" + orderType +
                ", order=" + order +
                ", error=" + error +
                ", orderStatus='" + orderStatus + '\'' +
                ", executionPrice=" + executionPrice +
                ", allocatedQuantity=" + allocatedQuantity +
                '}';
    }
}
