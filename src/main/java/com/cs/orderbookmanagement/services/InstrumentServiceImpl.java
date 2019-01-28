package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.repository.InstrumentRepository;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Override
    public InstrumentResponse addInstrument(InstrumentRequest executionRequest) {
        Execution addedInstrument = null;
        InstrumentResponse instrumentResponse = null;
        Execution execution = new Execution();
        execution.setInstrumentName(executionRequest.getInstrumentName());
        execution.setInstrumentDesc(executionRequest.getInstrumentDesc());
        addedInstrument = instrumentRepository.save(execution);
        if (addedInstrument == null) {
            instrumentResponse = new InstrumentResponse();
            instrumentResponse.setError(new Error(OrderBookConstants.OBMS_0002, OrderBookConstants.ERROR_IN_ADDING_INSTRUMENT));
        } else {
            instrumentResponse = new InstrumentResponse();
            instrumentResponse.setInstrumentId(addedInstrument.getInstrumentId());
        }
        return instrumentResponse;
    }


}
