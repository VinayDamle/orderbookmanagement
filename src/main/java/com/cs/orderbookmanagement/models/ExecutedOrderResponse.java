package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecutedOrderResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private OrderDetails orderDetails;

    public ExecutedOrderResponse(Error error, OrderDetails orderDetails) {
        this.error = error;
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "ExecutedOrderResponse{" +
                "error=" + error +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
