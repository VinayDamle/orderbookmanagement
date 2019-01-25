package com.cs.orderbookmanagement.services;

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
        OrderBook orderBook = null;
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
        String currentOrderBookStatus = null;
        if (!OrderBookConstants.OPEN.equalsIgnoreCase(command)) {
            currentOrderBookStatus = OrderBookConstants.CLOSE;
        } else {
            currentOrderBookStatus = OrderBookConstants.OPEN;
        }
        orderBook.setOrderBookStatus(currentOrderBookStatus);
        orderBookRepository.save(orderBook);
        return currentOrderBookStatus;
    }

    @Override
    public OrderDetails addOrderToOrderBook(Order order, int instrumentId) {
        OrderDetails addedOrder = null;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (orderBookHolder.isPresent()) {
            if (orderBookHolder.get().getOrderBookStatus() != null &&
                    orderBookHolder.get().getOrderBookStatus().equalsIgnoreCase(OrderBookConstants.CLOSE)) {
                addedOrder = new OrderDetails(new Error("OBMS_0006", OrderBookConstants.OBMS_ORD_BOOK_CLOSED_INSTID + " " + instrumentId));
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
                OrderDetails orderDetailsToSave = new OrderDetails(1, order.getInstrumentId(),
                        orderDao, null, 0, orderType, 0.0);
                addedOrder = orderDetailsRepository.save(orderDetailsToSave);
            }
        } else {
            addedOrder = new OrderDetails(new Error("OBMS_0001", OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        }
        return addedOrder;
    }

    @Override
    public ExecutedOrderResponse addExecutionAndProcessOrder(InstrumentRequest executionRequest, int instrumentId) {
        ExecutedOrderResponse executedOrderResponse = null;
        Optional<OrderBook> orderBookHolder = orderBookRepository.findById(instrumentId);
        if (!orderBookHolder.isPresent()) {
            executedOrderResponse = new ExecutedOrderResponse();
            executedOrderResponse.setError(new Error("OBMS_0001", OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        } else {
            if (orderBookHolder.get().getOrderBookStatus() != null &&
                    orderBookHolder.get().getOrderBookStatus().equalsIgnoreCase(OrderBookConstants.CLOSE)) {
                executedOrderResponse = new ExecutedOrderResponse();
                executedOrderResponse.setError(new Error("OBMS_0006", OrderBookConstants.OBMS_ORD_BOOK_CLOSED_INSTID + " " + instrumentId));
            } else {
                List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(instrumentId);
                if (orderDetailsList.isEmpty()) {
                    executedOrderResponse = new ExecutedOrderResponse();
                    executedOrderResponse.setError(new Error("OBMS_0001", OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
                    return executedOrderResponse;
                }
                Execution execution = new Execution();
                execution.setQuantity(executionRequest.getQuantity());
                execution.setExecutionPrice(executionRequest.getPrice());
                List<OrderDetails> unExecutedOrders = orderDetailsList;
                List<OrderDetails> validOrders = helper.getValidOrders(orderDetailsList, execution);
                validOrders = helper.getExecutedOrderDetails(validOrders, execution);
                executedOrderResponse = new ExecutedOrderResponse();
                executedOrderResponse.setOrderDetails(validOrders);
                List<OrderDetails> allOrders = new ArrayList<>(unExecutedOrders);
                allOrders.addAll(validOrders);
                orderDetailsRepository.saveAll(allOrders);
            }
        }
        return executedOrderResponse;
    }

}
