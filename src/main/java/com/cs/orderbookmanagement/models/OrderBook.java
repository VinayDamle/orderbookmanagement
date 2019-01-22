package com.cs.orderbookmanagement.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class OrderBook {

    @Id
    private int instrumentId;

    private String orderBookStatus;

    @OneToMany(mappedBy = "instrumentId")
    private Set<Execution> executions = new HashSet<>();

    @OneToMany(mappedBy = "order.instrumentId")
    private Set<OrderDetails> orderDetails = new HashSet<>();

    public OrderBook(int instrumentId, String orderBookStatus, Set<Execution> executions, Set<OrderDetails> orderDetails) {
        this.instrumentId = instrumentId;
        this.orderBookStatus = orderBookStatus;
        this.executions = executions;
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "instrumentId=" + instrumentId +
                ", orderBookStatus='" + orderBookStatus + '\'' +
                ", executions=" + executions +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
