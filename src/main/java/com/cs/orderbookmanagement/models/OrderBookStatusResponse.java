package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties
public class OrderBookStatusResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderBookStatus;

    public OrderBookStatusResponse(Error error, String orderBookStatus) {
        this.error = error;
        this.orderBookStatus = orderBookStatus;
    }

    @Override
    public String toString() {
        return "OrderBookStatusResponse{" +
                "error=" + error +
                ", orderBookStatus='" + orderBookStatus + '\'' +
                '}';
    }
}
