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
public class OBStatusResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer orderBookId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bookStatus;

    public OBStatusResponse(Error error) {
        this.error = error;
    }


}
