package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderBook;
import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.repository.OrderBookRepository;
import com.cs.orderbookmanagement.repository.OrderDetailsRepository;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookStatsticsServiceImplTests {

    private int instrumentId;

    @Mock
    private OrderBookHelper helper;

    @InjectMocks
    private StatsticsServiceImpl orderBookStatsticsService;

    @Mock
    private OrderBookRepository orderBookRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Test
    public void testFindAll() {
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1L);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderDetailsRepository.findAll()).thenReturn(orderDetailsList);
        List<OrderDetail> fetchedOrderDetailsList = orderDetailsRepository.findAll();
        Assertions.assertThat(fetchedOrderDetailsList).isNotNull();
        Assertions.assertThat(fetchedOrderDetailsList.size()).isEqualTo(3);
    }

    @Test
    public void testOrderDetailsByOrderInstrumentId() {
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1L);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(anyLong())).thenReturn(orderDetailsList);
        List<OrderDetail> fetchedOrderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(orderBook.getInstrumentId());
        Assertions.assertThat(fetchedOrderDetailsList).isNotNull();
        Assertions.assertThat(fetchedOrderDetailsList.size()).isEqualTo(3);
    }

    //NEED TO WRITE TEST CASE FOR getOrderStatstics

    public List<OrderDetail> getTestOrderDetails() {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        OrderDetail orderDetails = new OrderDetail();
        OrderDao order = new OrderDao(10, "", 1L, new BigDecimal(100));
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetail();
        order = new OrderDao(20, "", 1L, new BigDecimal(200));
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetail();
        order = new OrderDao(30, "", 1L, new BigDecimal(300));
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        return orderDetailsList;
    }
}
