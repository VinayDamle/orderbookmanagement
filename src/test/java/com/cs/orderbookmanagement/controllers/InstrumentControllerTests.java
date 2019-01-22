package com.cs.orderbookmanagement.controllers;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = InstrumentController.class, secure = false)
public class InstrumentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private MvcResult response;

    private DocumentContext context;

    private String instrumentRequestJsonPayload;

    private MockHttpServletRequestBuilder requestBuilder;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddInstrument() throws Exception {
        instrumentRequestJsonPayload = "{\"instrumentName\": \"Pen\", \"instrumentDesc\": \"Pen\"}";
        requestBuilder = MockMvcRequestBuilders.post("/instrument/").
                content(instrumentRequestJsonPayload).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.instrumentId").toString()).isNotNull();
        Assertions.assertThat(Integer.parseInt(context.read("$.instrumentId").toString())).isEqualTo(1);
    }

}
