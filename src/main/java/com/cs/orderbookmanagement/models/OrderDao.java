package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties
public class OrderDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private double price;

    private int quantity;

    private int instrumentId;

    private String entryDate;

    public OrderDao(int quantity, String entryDate, int instrumentId, double price) {
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