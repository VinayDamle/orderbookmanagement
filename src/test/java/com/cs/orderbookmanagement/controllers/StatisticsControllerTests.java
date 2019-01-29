package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.ExecuteOrderRequest;
import com.cs.orderbookmanagement.models.OrderRequest;
import com.cs.orderbookmanagement.models.OrderState;
import com.cs.orderbookmanagement.models.OrderStatstics;
import com.cs.orderbookmanagement.services.StatsticsService;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StatsticsController.class, secure = false)
public class StatisticsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private int instrumentId;

    private MvcResult response;

    private DocumentContext context;

    @MockBean
    private StatsticsService service;

    @MockBean
    private ExecuteOrderRequest executionRequest;

    private MockHttpServletRequestBuilder requestBuilder;

    @Test
    public void testGetAllOrders() throws Exception {
        instrumentId = 1;
        when(service.getAllOrders()).thenReturn(getTestOrderDetails());
        requestBuilder = get("/orderbook/statstics/orders").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(Integer.parseInt(context.read("$.[0].order.orderId").toString())).isEqualTo(1);
        assertThat(Integer.parseInt(context.read("$.[1].order.orderId").toString())).isEqualTo(2);
        assertThat(Integer.parseInt(context.read("$.[2].order.orderId").toString())).isEqualTo(3);
    }

    @Test
    public void testGetOrderDetailsByInstId() throws Exception {
        instrumentId = 1;
        when(service.getOrderDetailsByInstId(anyInt())).thenReturn(getTestOrderDetails());
        requestBuilder = get("/orderbook/statstics/" + instrumentId + "/orders").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(Integer.parseInt(context.read("$.[0].order.instrumentId").toString())).isEqualTo(1);
        assertThat(Integer.parseInt(context.read("$.[1].order.instrumentId").toString())).isEqualTo(1);
        assertThat(Integer.parseInt(context.read("$.[2].order.instrumentId").toString())).isEqualTo(1);
    }

    @Test
    public void testGetOrderDetailsByOrderId() throws Exception {
        int orderId = 1;
        when(service.getOrderDetailsByOrderId(anyInt())).thenReturn(getTestOrderDetails().get(0));
        requestBuilder = get("/orderbook/statstics/order/" + orderId).
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(Integer.parseInt(context.read("$.order.orderId").toString())).isEqualTo(1);
    }

    @Test
    public void testGetOrderStateByOrderId() throws Exception {
        int orderId = 1;
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        OrderDetail orderDetails = getTestOrderDetails().get(0);
        orderDetails.setExecutionPrice(100);
        orderDetails.getOrder().setPrice(80);
        orderDetails.setOrderStatus(OrderBookConstants.VALID);
        OrderState orderState = new OrderState(1, orderDetails.getOrder().getPrice(),
                orderDetails.getAllocatedQuantity(), orderDetails.getExecutionPrice(), orderDetails.getOrderStatus());
        when(service.getOrderStateByOrderId(anyInt())).thenReturn(orderState);
        requestBuilder = get("/orderbook/statstics/order/" + orderId + "/orderState").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(context.read("$.validityStatus").toString()).isEqualTo(orderDetails.getOrderStatus());
        assertThat(Integer.parseInt(context.read("$.orderId").toString())).isEqualTo(1);
        assertThat(Double.parseDouble(context.read("$.orderPrice").toString())).isEqualTo(orderDetails.getOrder().getPrice());
        assertThat(Double.parseDouble(context.read("$.executionPrice").toString())).isEqualTo(orderDetails.getExecutionPrice());
        assertThat(Integer.parseInt(context.read("$.executionQuantity").toString())).isEqualTo(orderDetails.getAllocatedQuantity());
    }

    @Test
    public void testGetOrderStatstics() throws Exception {
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        OrderStatstics orderStatstic = new OrderStatstics();
        orderStatstic.setEarliestOrderEntry(new OrderRequest(orderDetailsList.get(0).getOrder().getQuantity(), orderDetailsList.get(0).getOrder().getEntryDate(),
                orderDetailsList.get(0).getOrder().getInstrumentId(), orderDetailsList.get(0).getOrder().getPrice()));
        orderStatstic.setLatestOrderEntry(new OrderRequest(orderDetailsList.get(2).getOrder().getQuantity(), orderDetailsList.get(2).getOrder().getEntryDate(),
                orderDetailsList.get(2).getOrder().getInstrumentId(), orderDetailsList.get(2).getOrder().getPrice()));
        orderStatstic.setBiggestOrder(new OrderRequest(orderDetailsList.get(2).getOrder().getQuantity(), orderDetailsList.get(2).getOrder().getEntryDate(),
                orderDetailsList.get(2).getOrder().getInstrumentId(), orderDetailsList.get(2).getOrder().getPrice()));
        orderStatstic.setSmallestOrder(new OrderRequest(orderDetailsList.get(0).getOrder().getQuantity(), orderDetailsList.get(0).getOrder().getEntryDate(),
                orderDetailsList.get(0).getOrder().getInstrumentId(), orderDetailsList.get(0).getOrder().getPrice()));

        when(service.getOrderStatstics(false)).thenReturn(orderStatstic);
        requestBuilder = get("/orderbook/statstics").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());

        assertThat(Integer.parseInt(context.read("$.validOrders").toString())).isEqualTo(0);
        assertThat(Integer.parseInt(context.read("$.invalidOrders").toString())).isEqualTo(0);
        assertThat(context.read("$.biggestOrder.entryDate").toString()).isEqualTo("29-01-2019");
        assertThat(context.read("$.smallestOrder.entryDate").toString()).isEqualTo("24-01-2019");
        assertThat(context.read("$.latestOrderEntry.entryDate").toString()).isEqualTo("29-01-2019");
        assertThat(context.read("$.earliestOrderEntry.entryDate").toString()).isEqualTo("24-01-2019");
        assertThat(Integer.parseInt(context.read("$.biggestOrder.quantity").toString())).isEqualTo(30);
        assertThat(Integer.parseInt(context.read("$.smallestOrder.quantity").toString())).isEqualTo(10);
    }

    public List<OrderDetail> getTestOrderDetails() {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        OrderDetail orderDetails = new OrderDetail();
        OrderDao order = new OrderDao(10, "24-01-2019", 1, 100);
        order.setOrderId(1);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        orderDetails = new OrderDetail();
        order = new OrderDao(20, "28-01-2019", 1, 200);
        order.setOrderId(2);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        orderDetails = new OrderDetail();
        order = new OrderDao(30, "29-01-2019", 1, 300);
        order.setOrderId(3);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        return orderDetailsList;
    }

}
