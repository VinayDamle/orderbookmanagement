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

    @Override
    public String changeOrderBookStatus(int instrumentId, String command) {
        OrderBook orderBook;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (!orderBookHolder.isPresent()) {
            orderBook = new OrderBook();
            orderBook.setInstrumentId(instrumentId);
        } else {
            orderBook = orderBookHolder.get();
            if (orderBook.getOrderBookStatus() == null) {
                return OrderBookConstants.INSTRUMENT_ID_NOT_FOUND;
            }
        }
        orderBook.setInstrumentId(instrumentId);
        String currentOrderBookStatus;
        if (!OrderBookConstants.OPEN.equalsIgnoreCase(command)) {
            currentOrderBookStatus = OrderBookConstants.CLOSE;
        } else {
            currentOrderBookStatus = OrderBookConstants.OPEN;
        }
        orderBook.setOrderBookStatus(currentOrderBookStatus);
        orderBookRepository.save(orderBook);
        return currentOrderBookStatus;
    }

    /*@Override
    public OrderDetail addOrder(OrderRequest order, int instrumentId) {
        OrderDetail addedOrder;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (orderBookHolder.isPresent()) {
            if (orderBookHolder.get().getOrderBookStatus() != null &&
                    orderBookHolder.get().getOrderBookStatus().equalsIgnoreCase(OrderBookConstants.CLOSE)) {
                addedOrder = new OrderDetail(new Error(OrderBookConstants.OBMS_0006, OrderBookConstants.OBMS_ORD_BOOK_CLOSED_INSTID + " " + instrumentId));
            } else {
                OrderDao orderDao = new OrderDao();
                orderDao.setPrice(order.getPrice());
                orderDao.setQuantity(order.getQuantity());
                orderDao.setEntryDate(order.getEntryDate());
                orderDao.setInstrumentId(order.getInstrumentId());
                String orderType = OrderBookConstants.LIMIT_ORDER;
                if (order.getPrice() == 0) {
                    orderType = OrderBookConstants.MARKET_ORDER;
                }
                OrderDetail orderDetailsToSave = new OrderDetail(order.getInstrumentId(),
                        orderDao, null, 0, orderType, 0.0);
                addedOrder = orderDetailsRepository.save(orderDetailsToSave);
            }
        } else {
            addedOrder = new OrderDetail(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        }
        return addedOrder;
    }*/

    @Override
    public OrderResponse addOrder(OrderRequest order, int instrumentId) {
        OrderResponse orderResponse = null;
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
                String orderType = OrderBookConstants.LIMIT_ORDER;
                if (order.getPrice() == 0) {
                    orderType = OrderBookConstants.MARKET_ORDER;
                }
                OrderDetail orderDetailsToSave = new OrderDetail(order.getInstrumentId(),
                        orderDao, null, 0, orderType, 0.0);
                OrderDetail addedOrder = orderDetailsRepository.save(orderDetailsToSave);
                orderResponse = new OrderResponse();
                orderResponse.setOrder(addedOrder.getOrder());
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
    public ExecuteOrderResponse addExecutionAndProcessOrder(ExecuteOrderRequest executionRequest, int instrumentId) {
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
                executedOrderResponse.setOrderDetails(validOrders);
                List<OrderDetail> allOrders = new ArrayList<>(unExecutedOrders);
                allOrders.addAll(validOrders);
                orderDetailsRepository.saveAll(allOrders);
            }
        }
        return executedOrderResponse;
    }

}
