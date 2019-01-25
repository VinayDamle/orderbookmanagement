package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderBookController.class, secure = false)
public class OrderBookControllerTests {

    public static final String ORDER = "order/";
    public static final String EXECUTE = "execute/";

    @Autowired
    private MockMvc mockMvc;

    private int instrumentId;

    private MvcResult response;

    private DocumentContext context;

    @MockBean
    private OrderBookService service;

    @MockBean
    private InstrumentRequest executionRequest;

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
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookConstants.OPENED);
        openOrderBookRequestJsonPayload = getOpenOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + OrderBookConstants.STATUS + instrumentId).
                content(openOrderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isEqualTo(OrderBookConstants.OPENED);
    }

    @Test
    public void testOpenOrderBookWhenInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
        openOrderBookRequestJsonPayload = getOpenOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + OrderBookConstants.STATUS + instrumentId).
                content(openOrderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testCloseOrderBook() throws Exception {
        instrumentId = 1;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookConstants.CLOSED);
        closeOrderBookRequestJsonPayload = getCloseOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + OrderBookConstants.STATUS + instrumentId).
                content(closeOrderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderBookStatus").toString()).isEqualTo(OrderBookConstants.CLOSED);
    }

    @Test
    public void testCloseOrderBookWhenInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        when(service.changeOrderBookStatus(anyInt(), anyString())).thenReturn(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
        closeOrderBookRequestJsonPayload = getCloseOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + OrderBookConstants.STATUS + instrumentId).
                content(closeOrderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddOrder() throws Exception {
        instrumentId = 1;
        OrderDetails orderDetails = new OrderDetails(1, instrumentId,
                new OrderDao(10, "10-10-2019", instrumentId, 100.0),
                null, 0, OrderBookConstants.LIMIT_ORDER, 0.0);
        when(service.addOrderToOrderBook(any(Order.class), anyInt())).thenReturn(orderDetails);
        orderBookRequestJsonPayload = getOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + ORDER + instrumentId).
                content(orderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
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
        OrderDetails orderDetails = new OrderDetails(new Error(OrderBookConstants.OBMS_INVL_INST_ID, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        when(service.addOrderToOrderBook(any(Order.class), anyInt())).thenReturn(orderDetails);
        orderBookRequestJsonPayload = getOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + ORDER + instrumentId).
                content(orderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddExecutionAndExecuteOrder() throws Exception {
        instrumentId = 1;
        OrderDao order = new OrderDao(10, "10-10-2019", instrumentId, 100);
        OrderDetails orderDetails = new OrderDetails(1, instrumentId,
                order, "Valid", 10,
                executionRequest.getPrice() != 0 ? OrderBookConstants.LIMIT_ORDER : OrderBookConstants.MARKET_ORDER, 0.0);
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetails);
        ExecutedOrderResponse executedOrderResponse = new ExecutedOrderResponse();
        executedOrderResponse.setOrderDetails(orderDetailsList);
        when(executionRequest.getPrice()).thenReturn(100.0);
        when(service.addExecutionAndProcessOrder(any(InstrumentRequest.class), anyInt())).thenReturn(executedOrderResponse);
        orderBookExecutionRequestJsonPayload = getOrderBookExecutionRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + EXECUTE + instrumentId).
                content(orderBookExecutionRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderDetails").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderDetails.[0].orderStatus").toString()).isEqualTo(OrderBookConstants.VALID);
        Assertions.assertThat(context.read("$.orderDetails.[0].order.quantity").toString()).
                isLessThanOrEqualTo(context.read("$.orderDetails.[0].allocatedQuantity").toString());
    }

    @Test
    public void testAddExecutionAndExecuteOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10;
        ExecutedOrderResponse executedOrderResponse = new ExecutedOrderResponse();
        executedOrderResponse.setError(new Error(OrderBookConstants.OBMS_INVL_INST_ID, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));

        when(executionRequest.getPrice()).thenReturn(100.0);
        when(service.addExecutionAndProcessOrder(any(InstrumentRequest.class), anyInt())).thenReturn(executedOrderResponse);
        orderBookExecutionRequestJsonPayload = getOrderBookExecutionRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post(OrderBookConstants.ORDERBOOKMGMNT + EXECUTE + instrumentId).
                content(orderBookExecutionRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
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