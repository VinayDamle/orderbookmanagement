package com.cs.orderbookmanagement.models;

import com.cs.orderbookmanagement.entities.OrderBook;
import com.cs.orderbookmanagement.entities.OrderDao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties
public class OrderResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private Long orderDetailsId;

    private OrderRequest order;

    private OrderType orderType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderBook orderBook;

    private String orderStatus;

    private Integer allocatedQuantity;

    private BigDecimal executionPrice;

    public OrderResponse(Long instrumentId, OrderRequest order, String orderStatus, Integer allocatedQuantity, OrderType orderType, BigDecimal executionPrice) {
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
