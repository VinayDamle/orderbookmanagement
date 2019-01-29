package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecuteOrderRequest {

    @NotNull
    private int quantity;

    @NotNull
    private double price;

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
