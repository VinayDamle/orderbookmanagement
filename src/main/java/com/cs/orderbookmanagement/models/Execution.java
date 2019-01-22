package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties
public class Execution {

    private int quantity;

    @Id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected int instrumentId;

    private double executionPrice;

    public Execution(double executionPrice, int quantity, int instrumentId) {
        this.quantity = quantity;
        this.instrumentId = instrumentId;
        this.executionPrice = executionPrice;
    }

    @Override
    public String toString() {
        return "Execution{" +
                ", quantity=" + quantity +
                "executionPrice=" + executionPrice +
                ", instrumentId=" + instrumentId +
                '}';
    }
}
