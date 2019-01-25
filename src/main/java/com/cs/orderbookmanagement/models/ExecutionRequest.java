package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties
public class ExecutionRequest {

    @NotNull
    private int quantity;

    @NotNull
    private double price;

    private String instrumentName;

    private String instrumentDesc;

    @Override
    public String toString() {
        return "Execution{" +
                ", instrumentName=" + instrumentName +
                "instrumentDesc=" + instrumentDesc +
                '}';
    }
}
