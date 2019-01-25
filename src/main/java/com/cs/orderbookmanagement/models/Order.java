package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
final public class Order {

    private final double price;

    private final int quantity;

    private final int instrumentId;

    private final String entryDate;

    public Order(int quantity, String entryDate, int instrumentId, double price) {
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

    public static void main(String[] args) throws Exception {
        ObjectMapper m = new ObjectMapper();
        String s = m.writer().withDefaultPrettyPrinter().writeValueAsString(new Order(1, "", 1, 20));
        System.out.println(s);
    }

}