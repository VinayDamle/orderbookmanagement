package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderBookController.class, secure = false)
public class OrderBookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private Long instrumentId;

    private MvcResult response;

    private DocumentContext context;

    @MockBean
    private OrderBookService service;

    @MockBean
    private ExecuteOrderRequest executionRequest;

    private String orderBookRequestJsonPayload;

    private String openOrderBookRequestJsonPayload;

    private String closeOrderBookRequestJsonPayload;

    private String orderBookExecutionRequestJsonPayload;

    private MockHttpServletRequestBuilder requestBuilder;

    @Test
    public void testOpenOrderBook() throws Exception {
        when(service.openOrderBook()).thenReturn(new Long(1));
        requestBuilder = MockMvcRequestBuilders.post("/orderbook").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderBookId").toString()).isNotNull();
        Assertions.assertThat(Integer.parseInt(context.read("$.orderBookId").toString())).isEqualTo(1);
    }

    @Test
    public void testOpenOrderBookWhenInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10L;
        when(service.openOrderBook()).thenReturn(null);
        requestBuilder = MockMvcRequestBuilders.post("/orderbook").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
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
        instrumentId = 1L;
        when(service.closeOrderBook(anyLong())).thenReturn(OrderBookConstants.CLOSE);
        requestBuilder = MockMvcRequestBuilders.put("/orderbook/" + instrumentId).
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.bookStatus").toString()).isNotNull();
        Assertions.assertThat(context.read("$.bookStatus").toString()).isEqualTo(OrderBookConstants.CLOSE);
    }

    @Test
    public void testCloseOrderBookWhenInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10L;
        when(service.closeOrderBook(anyLong())).thenReturn(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
        requestBuilder = MockMvcRequestBuilders.put("/orderbook/" + instrumentId).
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.bookStatus").toString()).isNotNull();
        Assertions.assertThat(context.read("$.bookStatus").toString()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddOrder() throws Exception {
        instrumentId = 1L;
        OrderResponse orderDetails = new OrderResponse(instrumentId,
                new OrderRequest(10, "10-10-2019", instrumentId, new BigDecimal(100.0)),
                null, 0, OrderType.LIMIT_ORDER, new BigDecimal(0.0));
        orderDetails.setOrderDetailsId(1L);
        when(service.addOrder(any(OrderRequest.class), anyLong())).thenReturn(orderDetails);
        orderBookRequestJsonPayload = getOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post("/orderbook/" + instrumentId + "/order").
                content(orderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.order").toString()).isNotNull();
        Assertions.assertThat(Integer.parseInt(context.read("$.orderDetailsId").toString())).isEqualTo(1);
        Assertions.assertThat(Integer.parseInt(context.read("$.order.instrumentId").toString())).isEqualTo(1);
    }

    @Test
    public void testAddOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10L;
        OrderResponse orderDetails = new OrderResponse(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        when(service.addOrder(any(OrderRequest.class), anyLong())).thenReturn(orderDetails);
        orderBookRequestJsonPayload = getOrderBookRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post("/orderbook/" + instrumentId + "/order").
                content(orderBookRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isBadRequest()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.error").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isNotNull();
        Assertions.assertThat(context.read("$.error.errorMessage").toString()).isEqualTo(OrderBookConstants.UNEQUAL_INST_ID);
    }

    @Test
    public void testAddExecutionAndExecuteOrder() throws Exception {
        instrumentId = 1L;
        OrderDao order = new OrderDao(10, "10-10-2019", instrumentId, new BigDecimal(100));
        OrderDetail orderDetails = new OrderDetail(instrumentId,
                order, "Valid", 10,
                OrderType.LIMIT_ORDER, new BigDecimal(0.0));
        orderDetails.setOrderDetailsId(1L);
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(orderDetails);

        List<OrderResponse> orderResponses = new ArrayList<>();
        orderDetailsList.stream().forEach(vo -> {
            OrderResponse or = new OrderResponse();
            or.setOrderDetailsId(vo.getOrderDetailsId());
            or.setOrderStatus(vo.getOrderStatus());
            or.setOrderType(vo.getOrderType());
            or.setAllocatedQuantity(vo.getAllocatedQuantity());
            or.setExecutionPrice(vo.getExecutionPrice());
            OrderRequest orderRequest = new OrderRequest(vo.getOrder().getQuantity(), vo.getOrder().getEntryDate(), vo.getOrder().getInstrumentId(), vo.getOrder().getPrice());
            or.setOrder(orderRequest);
            orderResponses.add(or);
        });

        ExecuteOrderResponse executedOrderResponse = new ExecuteOrderResponse();
        executedOrderResponse.setOrderResponses(orderResponses);
        when(executionRequest.getPrice()).thenReturn(new BigDecimal(100.0));
        when(service.addExecutionAndProcessOrder(any(ExecuteOrderRequest.class), anyLong())).thenReturn(executedOrderResponse);
        orderBookExecutionRequestJsonPayload = getOrderBookExecutionRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post("/orderbook/" + instrumentId + "/execute").
                content(orderBookExecutionRequestJsonPayload).characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        Assertions.assertThat(context.read("$.orderResponses").toString()).isNotNull();
        Assertions.assertThat(context.read("$.orderResponses.[0].orderStatus").toString()).isEqualTo(OrderBookConstants.VALID);
        Assertions.assertThat(context.read("$.orderResponses.[0].order.quantity").toString()).
                isLessThanOrEqualTo(context.read("$.orderResponses.[0].allocatedQuantity").toString());
    }

    @Test
    public void testAddExecutionAndExecuteOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() throws Exception {
        instrumentId = 10L;
        ExecuteOrderResponse executedOrderResponse = new ExecuteOrderResponse();
        executedOrderResponse.setError(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));

        when(executionRequest.getPrice()).thenReturn(new BigDecimal(100.0));
        when(service.addExecutionAndProcessOrder(any(ExecuteOrderRequest.class), anyLong())).thenReturn(executedOrderResponse);
        orderBookExecutionRequestJsonPayload = getOrderBookExecutionRequestJsonPayload();
        requestBuilder = MockMvcRequestBuilders.post("/orderbook/" + instrumentId + "/execute").
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
