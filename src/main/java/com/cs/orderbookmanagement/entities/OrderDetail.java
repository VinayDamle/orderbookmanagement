package com.cs.orderbookmanagement.entities;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.OrderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OrderDetail {

    @Transient
    private Error error;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailsId;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderDao order;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumentId")
    private OrderBook orderBook;

    private String orderStatus;

    private Integer allocatedQuantity;

    private BigDecimal executionPrice;

    public OrderDetail(Long instrumentId, OrderDao order, String orderStatus, Integer allocatedQuantity, OrderType orderType, BigDecimal executionPrice) {
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
