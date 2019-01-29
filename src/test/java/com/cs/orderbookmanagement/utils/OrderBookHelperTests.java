package com.cs.orderbookmanagement.utils;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.entities.Execution;
import com.cs.orderbookmanagement.entities.OrderDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookHelperTests {

    @InjectMocks
    private OrderBookHelper helper;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFilterValidOrders() {
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        Execution execution = new Execution();
        execution.setQuantity(30);
        execution.setExecutionPrice(100.0);
        List<OrderDetail> validOrderDetailsList = helper.getValidOrders(orderDetailsList, execution);
        assertThat(validOrderDetailsList.get(0).getOrderStatus()).isEqualTo(OrderBookConstants.VALID);
        assertThat(validOrderDetailsList.get(1).getOrderStatus()).isEqualTo(OrderBookConstants.VALID);
        assertThat(validOrderDetailsList.get(2).getOrderStatus()).isEqualTo(OrderBookConstants.VALID);
    }

    @Test
    public void testGetExecutedOrderDetails() {
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        orderDetailsList.get(0).setOrderStatus(OrderBookConstants.VALID);
        orderDetailsList.get(1).setOrderStatus(OrderBookConstants.VALID);
        orderDetailsList.get(2).setOrderStatus(OrderBookConstants.VALID);
        Execution execution = new Execution();
        execution.setQuantity(30);
        execution.setExecutionPrice(100.0);
        helper.getExecutedOrderDetails(orderDetailsList, execution);
        assertThat(orderDetailsList.get(0).getAllocatedQuantity()).isEqualTo(5);
        assertThat(orderDetailsList.get(1).getAllocatedQuantity()).isEqualTo(10);
        assertThat(orderDetailsList.get(2).getAllocatedQuantity()).isEqualTo(15);
    }

    public List<OrderDetail> getTestOrderDetails() {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        OrderDetail orderDetails = new OrderDetail();
        OrderDao order = new OrderDao(10, "", 1, 100);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetail();
        order = new OrderDao(20, "", 1, 200);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetail();
        order = new OrderDao(30, "", 1, 300);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        return orderDetailsList;
    }
}
