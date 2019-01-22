package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderBookController.class, secure = false)
public class OrderBookControllerTests {


    @Autowired
    private MockMvc mockMvc;

    private int instrumentId;

    private MvcResult response;

    private DocumentContext context;

    @MockBean
    private OrderBookService service;

    @MockBean
    private ExecutionRequest executionRequest;

    private String orderBookRequestJsonPayload;

    private String openOrderBookRequestJsonPayload;

    private String closeOrderBookRequestJsonPayload;

    private String orderBookExecutionRequestJsonPayload;

    private MockHttpServletRequestBuilder requestBuilder;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testOpenOrderBook() throws Exception {
        instrumentId = 1;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookService.OPENED);
        openOrderBookRequestJsonPayload = getOpenOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "status/" + instrumentId).
                content(openOrderBookRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isEqualTo(OrderBookService.OPENED);
    }

    @Test
    public void testOpenOrderBookWhenInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookService.INSTRUMENT_ID_NOT_FOUND);
        openOrderBookRequestJsonPayload = getOpenOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "status/" + instrumentId).
                content(openOrderBookRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookService.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testCloseOrderBook() throws Exception {
        instrumentId = 1;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookService.CLOSED);
        closeOrderBookRequestJsonPayload = getCloseOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "status/" + instrumentId).
                content(closeOrderBookRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isEqualTo(OrderBookService.CLOSED);
    }

    @Test
    public void testCloseOrderBookWhenInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookService.INSTRUMENT_ID_NOT_FOUND);
        closeOrderBookRequestJsonPayload = getCloseOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "status/" + instrumentId).
                content(closeOrderBookRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookService.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddOrder() throws Exception {
        instrumentId = 1;
        OrderDetails orderDetails = new OrderDetails(1, instrumentId,
                new Order(10, "10-10-2019", instrumentId, 100.0),
                null, 0, OrderBookService.LIMIT_ORDER, null);
        when(service.addOrder(any(Order.class), anyInt())).thenReturn(orderDetails);
        when(service.addOrder(any(Order.class), anyInt())).thenReturn(orderDetails);
        orderBookRequestJsonPayload = getOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "order/" + instrumentId).
                content(orderBookRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.order").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderId").toString()).isNotNull();
        Assertions.assertThat(Integer.parseInt(context.read("$.orderId").toString())).isEqualTo(1);
        Assertions.assertThat(Integer.parseInt(context.read("$.order.instrumentId").toString())).isEqualTo(1);
    }

    @Test
    public void testAddOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        OrderDetails orderDetails = new OrderDetails(0, 0, null, null, 0, null,
                new Error(OrderBookService.OBMS_INVL_001, OrderBookService.INSTRUMENT_ID_NOT_FOUND));
        when(service.addOrder(any(Order.class), anyInt())).thenReturn(orderDetails);
        orderBookRequestJsonPayload = getOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "order/" + instrumentId).
                content(orderBookRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookService.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddExecutionAndExecuteOrder() throws Exception {
        instrumentId = 1;
        Order order = new Order(10, "10-10-2019", instrumentId, 100);
        OrderDetails orderDetails = new OrderDetails(1, instrumentId,
                order, "Valid", 10,
                executionRequest.getExecutionPrice() != 0 ? OrderBookService.LIMIT_ORDER : OrderBookService.MARKET_ORDER, null);
        ExecutedOrderResponse executedOrderResponse = new ExecutedOrderResponse(null, orderDetails);
        when(executionRequest.getExecutionPrice()).thenReturn(100.0);
        when(service.addExecutionAndProcessOrder(any(ExecutionRequest.class), anyInt())).thenReturn(executedOrderResponse);
        orderBookExecutionRequestJsonPayload = getOrderBookExecutionRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "execute/" + instrumentId).
                content(orderBookExecutionRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderDetails").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderDetails.orderStatus").toString()).isEqualTo(OrderBookService.VALID);
        Assertions.assertThat(context.read("$.orderDetails.order.quantity").toString()).
                isLessThanOrEqualTo(context.read("$.orderDetails.allocatedQuantity").toString());
    }

    @Test
    public void testAddExecutionAndExecuteOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        ExecutedOrderResponse executedOrderResponse = new ExecutedOrderResponse(
                new Error(OrderBookService.OBMS_INVL_001, OrderBookService.INSTRUMENT_ID_NOT_FOUND), null);
        when(executionRequest.getExecutionPrice()).thenReturn(100.0);
        when(service.addExecutionAndProcessOrder(any(ExecutionRequest.class), anyInt())).thenReturn(executedOrderResponse);
        orderBookExecutionRequestJsonPayload = getOrderBookExecutionRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookService.ORDERBOOKMGMNT + "execute/" + instrumentId).
                content(orderBookExecutionRequestJsonPayload).characterEncoding(OrderBookService.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookService.INSTRUMENT_ID_NOT_FOUND);
    }

    private String getOrderBookRequestJsonPayload() {
        return "{\"price\" : 20.0,\"quantity\" : 1,\"instrumentId\" : 1,\"entryDate\" : \"10/10/2019\"}";
    }

    private String getOrderBookExecutionRequestJsonPayload() {
        return "{\"quantity\" : 10,\"executionPrice\" : 100.0 }";
    }

    private String getOpenOrderBookRequestJsonPayload() {
        return "{\"orderBookStatusCommand\": \"open\"}";
    }

    private String getCloseOrderBookRequestJsonPayload() {
        return "{\"orderBookStatusCommand\": \"close\"}";
    }

}
