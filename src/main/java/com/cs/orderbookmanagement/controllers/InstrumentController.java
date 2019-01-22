package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.InstrumentRequest;
import com.cs.orderbookmanagement.models.InstrumentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/instrument")
public class InstrumentController {

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<InstrumentResponse> addInstrument(@RequestBody InstrumentRequest instrumentRequest) {
        InstrumentResponse instrumentResponse = new InstrumentResponse(null, 1);
        ResponseEntity<InstrumentResponse> entity = new ResponseEntity<>(
                instrumentResponse, HttpStatus.OK);
        return entity;
    }

}
