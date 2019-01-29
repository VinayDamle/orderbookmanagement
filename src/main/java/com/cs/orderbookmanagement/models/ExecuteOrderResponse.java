package com.cs.orderbookmanagement.models;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecuteOrderResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private List<OrderDetail> orderDetails;

    @Override
    public String toString() {
        return "ExecutedOrderResponse{" +
                "error=" + error +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
