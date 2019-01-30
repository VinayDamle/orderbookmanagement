package com.cs.orderbookmanagement.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Execution {

    private Integer quantity;

    private String instrumentName;

    private String instrumentDesc;

    @Id
    @GeneratedValue
    protected Long instrumentId;

    private BigDecimal executionPrice;

    public Execution(BigDecimal executionPrice, Integer quantity, Long instrumentId) {
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
