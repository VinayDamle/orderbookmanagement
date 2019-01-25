package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.InstrumentRequest;
import com.cs.orderbookmanagement.models.InstrumentResponse;

public interface InstrumentService {

    public InstrumentResponse addInstrument(InstrumentRequest instrumentRequest);

}
