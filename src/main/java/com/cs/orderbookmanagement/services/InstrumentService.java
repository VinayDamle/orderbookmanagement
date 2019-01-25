package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.ExecutionRequest;
import com.cs.orderbookmanagement.models.InstrumentResponse;

public interface InstrumentService {

    public InstrumentResponse addInstrument(ExecutionRequest executionRequest);

}
