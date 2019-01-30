package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecuteOrderRequest {

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
