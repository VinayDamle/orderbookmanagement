package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.InstrumentRequest;
import com.cs.orderbookmanagement.models.InstrumentResponse;
import com.cs.orderbookmanagement.services.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/instrument")
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<InstrumentResponse> addInstrument(@RequestBody InstrumentRequest instrumentRequest) {
        HttpStatus addInstrumentHttpStatus = HttpStatus.OK;
        InstrumentResponse instrumentResponse = instrumentService.addInstrument(instrumentRequest);
        System.out.println(instrumentResponse.getInstrumentId() + 1);
        if (instrumentResponse.getError() != null) {
            addInstrumentHttpStatus = HttpStatus.BAD_REQUEST;
        }
        ResponseEntity<InstrumentResponse> entity = new ResponseEntity<>(instrumentResponse, addInstrumentHttpStatus);
        return entity;
    }

}
