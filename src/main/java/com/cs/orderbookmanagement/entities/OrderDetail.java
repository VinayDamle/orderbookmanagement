package com.cs.orderbookmanagement.entities;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties
public class OrderDetail {

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailsId;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderDao order;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumentId")
    private OrderBook orderBook;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int allocatedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double executionPrice;

    public OrderDetail(int instrumentId, OrderDao order, String orderStatus, int allocatedQuantity, OrderType orderType, double executionPrice) {
        this.order = order;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.executionPrice = executionPrice;
        this.allocatedQuantity = allocatedQuantity;
    }

    public OrderDetail(Error error) {
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
