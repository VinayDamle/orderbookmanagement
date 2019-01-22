package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecutionRequest {

    private int quantity;

    private double executionPrice;

    public ExecutionRequest(double executionPrice, int quantity) {
        this.quantity = quantity;
        this.executionPrice = executionPrice;
    }

    @Override
    public String toString() {
        return "Execution{" +
                ", quantity=" + quantity +
                "executionPrice=" + executionPrice +
                '}';
    }
}
