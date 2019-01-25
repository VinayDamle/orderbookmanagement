package com.cs.orderbookmanagement.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OrderBook {

    @Id
    @GeneratedValue
    private int instrumentId;

    private String orderBookStatus;

    @OneToOne
    private Execution execution;

    @OneToMany
    private List<OrderDetails> orderDetails = new ArrayList<>();

    public OrderBook(int instrumentId, String orderBookStatus, Execution execution, List<OrderDetails> orderDetails) {
        this.instrumentId = instrumentId;
        this.orderBookStatus = orderBookStatus;
        this.execution = execution;
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "instrumentId=" + instrumentId +
                ", orderBookStatus='" + orderBookStatus + '\'' +
                ", execution=" + execution +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
