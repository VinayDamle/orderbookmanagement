package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.Execution;
import com.cs.orderbookmanagement.entities.OrderBook;
import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.repository.OrderBookRepository;
import com.cs.orderbookmanagement.repository.OrderDetailsRepository;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderBookServiceImpl implements OrderBookService {

    @Autowired
    private OrderBookHelper helper;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public OrderBookServiceImpl() {
        super();
    }

    @Override
    public Long openOrderBook() {
        Long orderBookId = null;
        OrderBook orderBook = new OrderBook();
        orderBook.setOrderBookStatus(OrderBookConstants.OPEN);
        OrderBook savedOrderBook = orderBookRepository.save(orderBook);
        if (savedOrderBook != null) {
            orderBookId = savedOrderBook.getInstrumentId();
        }
        return orderBookId;
    }

    @Override
    public String closeOrderBook(Long instrumentId) {
        OrderBook orderBook;
        String status = null;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (orderBookHolder == null || !orderBookHolder.isPresent()) {
            orderBook = new OrderBook();
            orderBook.setInstrumentId(instrumentId);
            status = OrderBookConstants.INSTRUMENT_ID_NOT_FOUND;
        } else {
            orderBook = orderBookHolder.get();
            orderBook.setOrderBookStatus(OrderBookConstants.CLOSE);
            OrderBook savedOrderBook = orderBookRepository.save(orderBook);
            if (savedOrderBook != null) {
                status = savedOrderBook.getOrderBookStatus();
            }
        }
        return status;
    }

    @Override
    public OrderResponse addOrder(OrderRequest order, Long instrumentId) {
        OrderResponse orderResponse;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (orderBookHolder.isPresent()) {
            if (orderBookHolder.get().getOrderBookStatus() != null &&
                    orderBookHolder.get().getOrderBookStatus().equalsIgnoreCase(OrderBookConstants.CLOSE)) {
                orderResponse = new OrderResponse(new Error(OrderBookConstants.OBMS_0006, OrderBookConstants.OBMS_ORD_BOOK_CLOSED_INSTID + " " + instrumentId));
            } else {
                OrderDao orderDao = new OrderDao();
                orderDao.setPrice(order.getPrice());
                orderDao.setQuantity(order.getQuantity());
                orderDao.setEntryDate(order.getEntryDate());
                orderDao.setInstrumentId(order.getInstrumentId());
                OrderType orderType = OrderType.LIMIT_ORDER;
                if (order.getPrice() == null || order.getPrice().intValue() == 0) { // Assuming nor price and 0 price orders are MARKET_ORDER
                    orderType = OrderType.MARKET_ORDER;
                }
                OrderDetail orderDetailsToSave = new OrderDetail(order.getInstrumentId(),
                        orderDao, null, 0, orderType, new BigDecimal(0.0));
                OrderDetail addedOrder = orderDetailsRepository.save(orderDetailsToSave);
                orderResponse = new OrderResponse();
                orderResponse.setOrder(new OrderRequest(addedOrder.getOrder().getQuantity(), addedOrder.getOrder().getEntryDate(), addedOrder.getOrder().getInstrumentId(), addedOrder.getOrder().getPrice()));
                orderResponse.setOrderType(addedOrder.getOrderType());
                orderResponse.setOrderStatus(addedOrder.getOrderStatus());
                orderResponse.setOrderDetailsId(addedOrder.getOrderDetailsId());
            }
        } else {
            orderResponse = new OrderResponse(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        }
        return orderResponse;
    }

    @Override
    public ExecuteOrderResponse addExecutionAndProcessOrder(ExecuteOrderRequest executionRequest, Long instrumentId) {
        ExecuteOrderResponse executedOrderResponse;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (!orderBookHolder.isPresent()) {
            executedOrderResponse = new ExecuteOrderResponse();
            executedOrderResponse.setError(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        } else {
            if (orderBookHolder.get().getOrderBookStatus() != null &&
                    orderBookHolder.get().getOrderBookStatus().equalsIgnoreCase(OrderBookConstants.CLOSE)) {
                executedOrderResponse = new ExecuteOrderResponse();
                executedOrderResponse.setError(new Error(OrderBookConstants.OBMS_0006, OrderBookConstants.OBMS_ORD_BOOK_CLOSED_INSTID + " " + instrumentId));
            } else {
                List<OrderDetail> orderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(instrumentId);
                if (orderDetailsList.isEmpty()) {
                    executedOrderResponse = new ExecuteOrderResponse();
                    executedOrderResponse.setError(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
                    return executedOrderResponse;
                }
                Execution execution = new Execution();
                execution.setQuantity(executionRequest.getQuantity());
                execution.setExecutionPrice(executionRequest.getPrice());
                List<OrderDetail> unExecutedOrders = orderDetailsList;
                List<OrderDetail> validOrders = helper.getValidOrders(orderDetailsList, execution);
                validOrders = helper.getExecutedOrderDetails(validOrders, execution);
                executedOrderResponse = new ExecuteOrderResponse();

                List<OrderResponse> orderResponses = new ArrayList<>();
                validOrders.stream().forEach(vo -> {
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
                executedOrderResponse.setOrderResponses(orderResponses);

                List<OrderDetail> allOrders = new ArrayList<>(unExecutedOrders);
                allOrders.addAll(validOrders);
                orderDetailsRepository.saveAll(allOrders);
            }
        }
        return executedOrderResponse;
    }

}
