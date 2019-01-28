package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.Execution;
import com.cs.orderbookmanagement.models.ExecutionRequest;
import com.cs.orderbookmanagement.models.InstrumentRequest;
import com.cs.orderbookmanagement.models.InstrumentResponse;
import com.cs.orderbookmanagement.repository.InstrumentRepository;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentServiceImplTests {

    private int instrumentId;

    @Mock
    private OrderBookHelper helper;

    @InjectMocks
    private InstrumentServiceImpl instrumentService;

    @Mock
    private InstrumentRepository instrumentRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddInstrument() {
        Execution execution = new Execution();
        execution.setInstrumentId(1);
        InstrumentRequest executionRequest = new InstrumentRequest();
        executionRequest.setPrice(50);
        executionRequest.setQuantity(30);
        executionRequest.setInstrumentDesc("");
        executionRequest.setInstrumentName("");
        when(instrumentRepository.save(any(Execution.class))).thenReturn(execution);
        InstrumentResponse addedInstrument = instrumentService.addInstrument(executionRequest);
        Assertions.assertThat(addedInstrument).isNotNull();
        Assertions.assertThat(addedInstrument.getError()).isNull();
        Assertions.assertThat(addedInstrument.getInstrumentId()).isEqualTo(1);
    }
}
