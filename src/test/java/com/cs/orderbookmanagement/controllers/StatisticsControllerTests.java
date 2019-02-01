package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
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
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
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
        when(service.getAllOrders()).thenReturn(orderResponses);
        requestBuilder = get("/orderbook/statstics/orders").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(context.read("$.[0].order").toString()).isNotNull();
        assertThat(context.read("$.[0].order").toString()).isNotNull();
        assertThat(context.read("$.[0].order").toString()).isNotNull();
    }

    @Test
    public void testGetOrderDetailsByInstId() throws Exception {
        instrumentId = 1;
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
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
        when(service.getOrderDetailsByInstId(anyLong())).thenReturn(orderResponses);
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
        OrderDetail orderDetails = getTestOrderDetails().get(0);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrder(new OrderRequest(orderDetails.getOrder().getQuantity(), orderDetails.getOrder().getEntryDate(),
                orderDetails.getOrder().getInstrumentId(), orderDetails.getOrder().getPrice()));
        orderResponse.setOrderStatus(orderDetails.getOrderStatus());
        orderResponse.setOrderType(orderDetails.getOrderType());
        orderResponse.setAllocatedQuantity(orderDetails.getAllocatedQuantity());
        orderResponse.setExecutionPrice(orderDetails.getExecutionPrice());
        when(service.getOrderDetailsByOrderId(anyLong())).thenReturn(orderResponse);
        requestBuilder = get("/orderbook/statstics/order/" + orderId).
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(Integer.parseInt(context.read("$.order.instrumentId").toString())).isEqualTo(1);
    }

    @Test
    public void testGetOrderStateByOrderId() throws Exception {
        int orderId = 1;
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        OrderDetail orderDetails = getTestOrderDetails().get(0);
        orderDetails.setExecutionPrice(new BigDecimal(100));
        orderDetails.getOrder().setPrice(new BigDecimal(80));
        orderDetails.setOrderStatus(OrderBookConstants.VALID);
        OrderState orderState = new OrderState(1L, orderDetails.getOrder().getPrice(),
                8, orderDetails.getExecutionPrice(), orderDetails.getOrderStatus());
        when(service.getOrderStateByOrderId(anyLong())).thenReturn(orderState);
        requestBuilder = get("/orderbook/statstics/order/" + orderId + "/orderState").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).param("instrumentId", "1");
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());
        assertThat(context.read("$.validityStatus").toString()).isEqualTo(orderDetails.getOrderStatus());
        assertThat(Integer.parseInt(context.read("$.orderId").toString())).isEqualTo(1L);
        assertThat(Double.parseDouble(context.read("$.orderPrice").toString())).isEqualTo(orderDetails.getOrder().getPrice().doubleValue());
        assertThat(Double.parseDouble(context.read("$.executionPrice").toString())).isEqualTo(orderDetails.getExecutionPrice().doubleValue());
        assertThat(Integer.parseInt(context.read("$.executionQuantity").toString())).isEqualTo(8);
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
        orderStatstic.setValidOrders(2);
        orderStatstic.setInvalidOrders(4);
        orderStatstic.setTotalNoOfOrders(10);
        when(service.getOrderStatstics(false)).thenReturn(orderStatstic);
        requestBuilder = get("/orderbook/statstics").
                characterEncoding(OrderBookConstants.UTF_8).contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON);
        response = mockMvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).
                andReturn();
        context = JsonPath.parse(response.getResponse().getContentAsString());

        assertThat(Integer.parseInt(context.read("$.validOrders").toString())).isEqualTo(2);
        assertThat(Integer.parseInt(context.read("$.invalidOrders").toString())).isEqualTo(4);
        assertThat(context.read("$.biggestOrder.entryDate").equals(LocalDate.now().plusDays(2)));
        assertThat(context.read("$.smallestOrder.entryDate").equals(LocalDate.now().minusDays(2)));
        assertThat(context.read("$.latestOrderEntry.entryDate").equals(LocalDate.now().plusDays(2)));
        assertThat(context.read("$.earliestOrderEntry.entryDate").equals(LocalDate.now().minusDays(2)));
        assertThat(Integer.parseInt(context.read("$.biggestOrder.quantity").toString())).isEqualTo(30);
        assertThat(Integer.parseInt(context.read("$.smallestOrder.quantity").toString())).isEqualTo(10);
        assertThat(Integer.parseInt(context.read("$.totalNoOfOrders").toString())).isEqualTo(10);
    }

    public List<OrderDetail> getTestOrderDetails() {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        OrderDetail orderDetails = new OrderDetail();
        OrderDao order = new OrderDao(10, LocalDate.now().minusDays(2), 1L, new BigDecimal(100));
        order.setOrderId(1L);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        orderDetails = new OrderDetail();
        order = new OrderDao(20, LocalDate.now(), 1L, new BigDecimal(200));
        order.setOrderId(2L);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        orderDetails = new OrderDetail();
        order = new OrderDao(30, LocalDate.now().plusDays(2), 1L, new BigDecimal(300));
        order.setOrderId(3L);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        return orderDetailsList;
    }

}
