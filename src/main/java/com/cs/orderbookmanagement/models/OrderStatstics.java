package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
public class OrderStatstics {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    private int totalNoOfOrders;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Order biggestOrder;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Order smallestOrder;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int validOrders;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int invalidOrders;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Order latestOrderEntry;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Order earliestOrderEntry;

    public OrderStatstics() {
    }

    public OrderStatstics(Error error) {
        this.error = error;
    }



}
