package com.cs.orderbookmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class InstrumentResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int instrumentId;

    public InstrumentResponse(Error error, int instrumentId) {
        this.error = error;
        this.instrumentId = instrumentId;
    }

    @Override
    public String toString() {
        return "InstrumentResponse{" +
                "error=" + error +
                ", instrumentId='" + instrumentId + '\'' +
                '}';
    }
}
