package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.ExecutedOrderResponse;
import com.cs.orderbookmanagement.models.ExecutionRequest;
import com.cs.orderbookmanagement.models.Order;
import com.cs.orderbookmanagement.models.OrderDetails;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookServiceImplTests {

    private int instrumentId;

    @InjectMocks
    private OrderBookServiceImpl orderBookService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testOpenOrderBook() {
        Assertions.assertThat(orderBookService.changeOrderBookStatus(1, "open")).isEqualTo(OrderBookService.OPENED);
    }

    @Test
    public void testCloseOrderBook() {
        Assertions.assertThat(orderBookService.changeOrderBookStatus(1, "close")).isEqualTo(OrderBookService.CLOSED);
    }

    @Test
    public void testAddOrder() {
        OrderDetails orderDetails = orderBookService.addOrder(new Order(10, "10/10/2019", 1, 100), 1);
        Assertions.assertThat(orderDetails).isNotNull();
        Assertions.assertThat(orderDetails.getError()).isNull();
        Assertions.assertThat(orderDetails.getOrderId()).isEqualTo(1);
    }

    @Test
    public void testAddExecution() {
        ExecutedOrderResponse executedOrderResponse = orderBookService.addExecutionAndProcessOrder(new ExecutionRequest(100, 10), 1);
        Assertions.assertThat(executedOrderResponse).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError()).isNull();
    }
}
