package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecutedOrderResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private List<OrderDetails> orderDetails;

    @Override
    public String toString() {
        return "ExecutedOrderResponse{" +
                "error=" + error +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
