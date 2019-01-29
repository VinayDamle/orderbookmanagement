package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderBook;
import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.*;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderBookServiceImplTests {

    private int instrumentId;

    @Mock
    private OrderBookHelper helper;

    @InjectMocks
    private OrderBookServiceImpl orderBookService;

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
    public void testOpenOrderBook() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderBookRepository.findById(anyInt())).thenReturn(Optional.of(orderBook));
        Assertions.assertThat(orderBookService.changeOrderBookStatus(1, OrderBookConstants.OPEN)).isEqualTo(OrderBookConstants.OPEN);
    }

    @Test
    public void testOpenOrderBookWhenInstrumentIdDoesNotExistsThenReturnNotFound() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(111);
        orderBook.setOrderBookStatus(null);
        when(orderBookRepository.findById(111)).thenReturn(Optional.of(orderBook));
        String orderBookStatus = orderBookService.changeOrderBookStatus(111, OrderBookConstants.OPEN);
        Assertions.assertThat(orderBookStatus).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testCloseOrderBook() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1);
        orderBook.setOrderBookStatus(OrderBookConstants.CLOSE);
        when(orderBookRepository.findById(anyInt())).thenReturn(Optional.of(orderBook));
        Assertions.assertThat(orderBookService.changeOrderBookStatus(1, OrderBookConstants.CLOSE)).isEqualTo(OrderBookConstants.CLOSE);
    }

    @Test
    public void testCloseOrderBookWhenInstrumentIdDoesNotExistsThenReturnNotFound() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(111);
        orderBook.setOrderBookStatus(null);
        when(orderBookRepository.findById(111)).thenReturn(Optional.of(orderBook));
        String orderBookStatus = orderBookService.changeOrderBookStatus(111, OrderBookConstants.CLOSE);
        Assertions.assertThat(orderBookStatus).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddOrder() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderBookRepository.findById(anyInt())).thenReturn(Optional.of(orderBook));

        OrderDetail orderDetails = new OrderDetail();
        //orderDetails.setOrderId(1);
        when(orderDetailsRepository.save(any(OrderDetail.class))).thenReturn(orderDetails);
        OrderResponse addedRrderDetails = orderBookService.addOrder(new OrderRequest(10, "10/10/2019", 1, 100), 1);
        Assertions.assertThat(addedRrderDetails).isNotNull();
        Assertions.assertThat(addedRrderDetails.getError()).isNull();
        //Assertions.assertThat(addedRrderDetails.getOrderId()).isEqualTo(1);
    }

    @Test
    public void testAddOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() {
        OrderDetail orderDetails = new OrderDetail();
        //orderDetails.setOrderId(1);
        OrderResponse addedRrderDetails = orderBookService.addOrder(new OrderRequest(10, "10/10/2019", 1, 100), 1);
        Assertions.assertThat(addedRrderDetails).isNotNull();
        Assertions.assertThat(addedRrderDetails.getError()).isNotNull();
        Assertions.assertThat(addedRrderDetails.getError().getErrorMessage()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);

    }

    @Test
    public void testAddExecution() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderBookRepository.findById(anyInt())).thenReturn(Optional.of(orderBook));
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        when(orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(anyInt())).thenReturn(orderDetailsList);

        ExecuteOrderRequest execution = new ExecuteOrderRequest();
        execution.setPrice(30);
        execution.setPrice(100.0);
        List<OrderDetail> validOrders = getTestOrderDetails();
        validOrders.forEach(vod -> vod.setOrderStatus(OrderBookConstants.VALID));
        validOrders.get(0).setAllocatedQuantity(5);
        validOrders.get(1).setAllocatedQuantity(10);
        validOrders.get(2).setAllocatedQuantity(15);
        ExecuteOrderResponse executedOrderResponse = orderBookService.addExecutionAndProcessOrder(execution, 1);
        Assertions.assertThat(executedOrderResponse).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError()).isNull();
    }

    @Test
    public void testAddExecutionForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() {
        ExecuteOrderRequest execution = new ExecuteOrderRequest();
        execution.setPrice(30);
        execution.setPrice(100.0);
        List<OrderDetail> validOrders = getTestOrderDetails();
        validOrders.forEach(vod -> vod.setOrderStatus(OrderBookConstants.VALID));
        validOrders.get(0).setAllocatedQuantity(5);
        validOrders.get(1).setAllocatedQuantity(10);
        validOrders.get(2).setAllocatedQuantity(15);
        ExecuteOrderResponse executedOrderResponse = orderBookService.addExecutionAndProcessOrder(execution, 1);
        Assertions.assertThat(executedOrderResponse).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError()).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError().getErrorMessage()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
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
