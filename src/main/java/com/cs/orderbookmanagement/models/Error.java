package com.cs.orderbookmanagement.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {

    private String errorCode;

    private String errorMessage;

    public Error(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Error{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
