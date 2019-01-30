package com.cs.orderbookmanagement.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OrderDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private BigDecimal price;

    private Integer quantity;

    private Long instrumentId;

    private String entryDate;

    public OrderDao(Integer quantity, String entryDate, Long instrumentId, BigDecimal price) {
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