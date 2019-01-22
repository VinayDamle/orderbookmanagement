package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class InstrumentRequest {

    private int instrumentId;

    private String instrumentName;

    private String instrumentDesc;

    @Override
    public String toString() {
        return "InstrumentRequest{" +
                "instrumentId=" + instrumentId +
                ", instrumentName=" + instrumentName +
                ", instrumentDesc=" + instrumentDesc +
                '}';
    }
}
