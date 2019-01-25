package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.OrderBook;
import com.cs.orderbookmanagement.models.OrderDao;
import com.cs.orderbookmanagement.models.OrderDetails;
import com.cs.orderbookmanagement.repository.OrderBookRepository;
import com.cs.orderbookmanagement.repository.OrderDetailsRepository;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookStatsticsServiceImplTests {

    private int instrumentId;

    @Mock
    private OrderBookHelper helper;

    @InjectMocks
    private OrderDetailsStatsticsServiceImpl orderBookStatsticsService;

    @Mock
    private OrderBookRepository orderBookRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindAll() {
        List<OrderDetails> orderDetailsList = getTestOrderDetails();
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderDetailsRepository.findAll()).thenReturn(orderDetailsList);
        List<OrderDetails> fetchedOrderDetailsList = orderDetailsRepository.findAll();
        Assertions.assertThat(fetchedOrderDetailsList).isNotNull();
        Assertions.assertThat(fetchedOrderDetailsList.size()).isEqualTo(3);
    }

    @Test
    public void testOrderDetailsByOrderInstrumentId() {
        List<OrderDetails> orderDetailsList = getTestOrderDetails();
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(anyInt())).thenReturn(orderDetailsList);
        List<OrderDetails> fetchedOrderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(orderBook.getInstrumentId());
        Assertions.assertThat(fetchedOrderDetailsList).isNotNull();
        Assertions.assertThat(fetchedOrderDetailsList.size()).isEqualTo(3);
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
