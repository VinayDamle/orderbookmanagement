package com.cs.orderbookmanagement.utils;

import com.cs.orderbookmanagement.models.Execution;
import com.cs.orderbookmanagement.models.OrderDao;
import com.cs.orderbookmanagement.models.OrderDetails;
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
        List<OrderDetails> orderDetailsList = getTestOrderDetails();
        Execution execution = new Execution();
        execution.setQuantity(30);
        execution.setExecutionPrice(100.0);
        helper.getValidOrders(orderDetailsList, execution);
        assertThat(orderDetailsList.get(0).getOrderStatus()).isEqualTo(OrderBookConstants.VALID);
        assertThat(orderDetailsList.get(1).getOrderStatus()).isEqualTo(OrderBookConstants.VALID);
        assertThat(orderDetailsList.get(2).getOrderStatus()).isEqualTo(OrderBookConstants.VALID);
    }

    @Test
    public void testGetExecutedOrderDetails() {
        List<OrderDetails> orderDetailsList = getTestOrderDetails();
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

    public List<OrderDetails> getTestOrderDetails() {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        OrderDetails orderDetails = new OrderDetails();
        OrderDao order = new OrderDao(10, "", 1, 100);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetails();
        order = new OrderDao(20, "", 1, 200);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetails();
        order = new OrderDao(30, "", 1, 300);
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        return orderDetailsList;
    }
}
