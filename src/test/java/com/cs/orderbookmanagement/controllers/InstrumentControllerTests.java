package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.ExecutionRequest;
import com.cs.orderbookmanagement.models.InstrumentResponse;
import com.cs.orderbookmanagement.services.InstrumentService;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = InstrumentController.class, secure = false)
public class InstrumentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult response;

    private DocumentContext context;

    @MockBean
    private InstrumentService instrumentService;

    private String instrumentRequestJsonPayload;

    private MockHttpServletRequestBuilder requestBuilder;

    @Test
    public void testAddInstrument() throws Exception {
        int instrumentId = 1;
        InstrumentResponse instrumentResponse = new InstrumentResponse();
        instrumentResponse.setInstrumentId(instrumentId);
        when(instrumentService.addInstrument(any(ExecutionRequest.class))).thenReturn(instrumentResponse);
        instrumentRequestJsonPayload = "{\"instrumentName\": \"Pen\", \"instrumentDesc\": \"Pen\"}";
        requestBuilder = MockMvcRequestBuilders.post("/instrument/").
                content(instrumentRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(Integer.parseInt(context.read("$.instrumentId").toString())).isEqualTo(instrumentId);
    }

}
