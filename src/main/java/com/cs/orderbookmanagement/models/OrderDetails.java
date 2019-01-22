package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties
public class OrderDetails {

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    @Embedded
    private Order order;

    @Id
    private int orderId;

    private String orderType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int allocatedQuantity;

    public OrderDetails(int orderId, int instrumentId, Order order, String orderStatus, int allocatedQuantity, String orderType, Error error) {
        this.error = error;
        this.order = order;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.allocatedQuantity = allocatedQuantity;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId=" + orderId +
                ", orderType=" + orderType +
                ", order=" + order +
                ", error=" + error +
                ", orderStatus='" + orderStatus + '\'' +
                ", allocatedQuantity=" + allocatedQuantity +
                '}';
    }
}
