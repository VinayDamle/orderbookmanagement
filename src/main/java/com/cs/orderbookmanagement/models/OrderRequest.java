package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties
final public class OrderRequest {

    private final BigDecimal price;

    private final Integer quantity;

    private final Long instrumentId;

    private final String entryDate;

    public OrderRequest(Integer quantity, String entryDate, Long instrumentId, BigDecimal price) {
        this.price = price;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.instrumentId = instrumentId;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", price=" + price +
                "quantity=" + quantity +
                ", entryDate=" + entryDate +
                ", instrumentId=" + instrumentId +
                '}';
    }

}