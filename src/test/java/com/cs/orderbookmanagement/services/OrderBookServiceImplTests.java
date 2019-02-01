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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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
    public void setUp() {
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testOpenOrderBook() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1L);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderBookRepository.save(any(OrderBook.class))).thenReturn(orderBook);
        Long orderBookId = orderBookService.openOrderBook();
        Assertions.assertThat(orderBookId).isEqualTo(1);
    }

    @Test
    public void testOpenOrderBookWhenInstrumentIdDoesNotExistsThenReturnNotFound() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(111L);
        orderBook.setOrderBookStatus(null);
        Long orderBookId = orderBookService.openOrderBook();
        Assertions.assertThat(orderBookId).isNull();
    }

    @Test
    public void testCloseOrderBook() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1L);
        orderBook.setOrderBookStatus(OrderBookConstants.CLOSE);
        when(orderBookRepository.save(any(OrderBook.class))).thenReturn(orderBook);
        when(orderBookRepository.findById(anyLong())).thenReturn(Optional.of(orderBook));
        Assertions.assertThat(orderBookService.closeOrderBook(1L)).isEqualTo(OrderBookConstants.CLOSE);
    }

    @Test
    public void testCloseOrderBookWhenInstrumentIdDoesNotExistsThenReturnNotFound() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(111L);
        orderBook.setOrderBookStatus(null);
        //when(orderBookRepository.save(any(OrderBook.class))).thenReturn(null);
        when(orderBookRepository.findById(111L)).thenReturn(null);
        String orderBookStatus = orderBookService.closeOrderBook(111L);
        Assertions.assertThat(orderBookStatus).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    @Test
    public void testAddOrder() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1L);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderBookRepository.findById(anyLong())).thenReturn(Optional.of(orderBook));

        OrderDetail orderDetails = new OrderDetail();
        OrderDao orderDao = new OrderDao();
        orderDao.setPrice(new BigDecimal(20));
        orderDao.setInstrumentId(1L);
        orderDao.setQuantity(10);
        orderDetails.setOrder(orderDao);

        when(orderDetailsRepository.save(any(OrderDetail.class))).thenReturn(orderDetails);
        OrderResponse addedRrderDetails = orderBookService.addOrder(new OrderRequest(10, LocalDate.now(), 1L, new BigDecimal(100)), 1L);
        Assertions.assertThat(addedRrderDetails).isNotNull();
        Assertions.assertThat(addedRrderDetails.getError()).isNull();
    }

    @Test
    public void testAddOrderForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() {
        OrderDetail orderDetails = new OrderDetail();
        OrderResponse addedRrderDetails = orderBookService.addOrder(new OrderRequest(10, LocalDate.now(), 1L, new BigDecimal(100)), 1L);
        Assertions.assertThat(addedRrderDetails).isNotNull();
        Assertions.assertThat(addedRrderDetails.getError()).isNotNull();
        Assertions.assertThat(addedRrderDetails.getError().getErrorMessage()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);

    }

    @Test
    public void testAddExecution() {
        OrderBook orderBook = new OrderBook();
        orderBook.setInstrumentId(1L);
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        when(orderBookRepository.findById(anyLong())).thenReturn(Optional.of(orderBook));
        List<OrderDetail> orderDetailsList = getTestOrderDetails();
        when(orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(anyLong())).thenReturn(orderDetailsList);

        ExecuteOrderRequest execution = new ExecuteOrderRequest();
        execution.setPrice(new BigDecimal(30));
        execution.setPrice(new BigDecimal(100.0));
        List<OrderDetail> validOrders = getTestOrderDetails();
        validOrders.forEach(vod -> vod.setOrderStatus(OrderBookConstants.VALID));
        validOrders.get(0).setAllocatedQuantity(5);
        validOrders.get(1).setAllocatedQuantity(10);
        validOrders.get(2).setAllocatedQuantity(15);
        ExecuteOrderResponse executedOrderResponse = orderBookService.addExecutionAndProcessOrder(execution, 1L);
        Assertions.assertThat(executedOrderResponse).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError()).isNull();
    }

    @Test
    public void testAddExecutionForAnInvalidInstrumentIdThenReturnInstrumentIdNotFound() {
        ExecuteOrderRequest execution = new ExecuteOrderRequest();
        execution.setPrice(new BigDecimal(30));
        execution.setPrice(new BigDecimal(100.0));
        List<OrderDetail> validOrders = getTestOrderDetails();
        validOrders.forEach(vod -> vod.setOrderStatus(OrderBookConstants.VALID));
        validOrders.get(0).setAllocatedQuantity(5);
        validOrders.get(1).setAllocatedQuantity(10);
        validOrders.get(2).setAllocatedQuantity(15);
        ExecuteOrderResponse executedOrderResponse = orderBookService.addExecutionAndProcessOrder(execution, 1L);
        Assertions.assertThat(executedOrderResponse).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError()).isNotNull();
        Assertions.assertThat(executedOrderResponse.getError().getErrorMessage()).isEqualTo(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND);
    }

    public List<OrderDetail> getTestOrderDetails() {
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        OrderDetail orderDetails = new OrderDetail();
        OrderDao order = new OrderDao(10, LocalDate.now(), 1L, new BigDecimal(100));
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetail();
        order = new OrderDao(20, LocalDate.now().minusDays(2), 1L, new BigDecimal(200));
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);
        orderDetails = new OrderDetail();
        order = new OrderDao(30, LocalDate.now().plusDays(2), 1L, new BigDecimal(300));
        orderDetails.setOrder(order);
        orderDetailsList.add(orderDetails);

        return orderDetailsList;
    }
}
