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

import java.util.*;

@Slf4j
@Service
public class OrderDetailsStatsticsServiceImpl implements OrderDetailsStatsticsService {

    @Autowired
    private OrderBookHelper helper;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<OrderDetails> getAllOrders() {
        return orderDetailsRepository.findAll();
    }

    public OrderStatstics getOrderStatstics(boolean fetchValidInvalidRecords) {
        OrderStatstics osc = new OrderStatstics();
        List<OrderDetails> ods = orderDetailsRepository.findAll();
        if (ods == null || ods.isEmpty()) {
            osc.setError(new Error("", "No order found."));
            return osc;
        }
        osc = new OrderStatstics();
        osc.setTotalNoOfOrders(ods.size());
        sortByOrderByQuantityAsc(ods);
        OrderDao orderDao = ods.get(0).getOrder();
        Order biggestOrder = new Order(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setBiggestOrder(biggestOrder);
        orderDao = ods.get(ods.size() - 1).getOrder();
        Order smallestOrder = new Order(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setSmallestOrder(smallestOrder);
        sortByOrderByEntryDateAsc(ods);
        orderDao = ods.get(0).getOrder();
        Order earliestOrder = new Order(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setEarliestOrderEntry(earliestOrder);
        orderDao = ods.get(ods.size() - 1).getOrder();
        Order latestOrder = new Order(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setLatestOrderEntry(latestOrder);
        if (fetchValidInvalidRecords) {
            setAdditionalRecords(osc, ods);
        }
        return osc;
    }

    private void setAdditionalRecords(OrderStatstics osc, List<OrderDetails> ods) {
        for (OrderDetails od : ods) {
            int validOrderCount = 0;
            int invalidOrderCount = 0;
            if (od.getOrderStatus() != null && OrderBookConstants.VALID.equalsIgnoreCase(od.getOrderStatus())) {
                validOrderCount++;
            } else {
                invalidOrderCount++;
            }
            osc.setValidOrders(validOrderCount);
            osc.setInvalidOrders(invalidOrderCount);
        }
    }


    private void sortByOrderByQuantityAsc(List<OrderDetails> unSortedOrderDetails) {
        Collections.sort(unSortedOrderDetails, (o1, o2) -> o2.getOrder().getQuantity() - o1.getOrder().getQuantity());
    }

    private void sortByOrderByEntryDateAsc(List<OrderDetails> unSortedOrderDetails) {
        Collections.sort(unSortedOrderDetails, new Comparator<OrderDetails>() {
            @Override
            public int compare(OrderDetails o1, OrderDetails o2) {
                return o1.getOrder().getEntryDate().compareTo(o2.getOrder().getEntryDate());
            }
        });
    }

    @Override
    public List<OrderDetails> getOrderDetailsByInstId(int instrumentId) {
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(instrumentId);
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setError(new Error(OrderBookConstants.OBMS_0003, "Order not found for instrumentId " + instrumentId));
            if (orderDetailsList == null) {
                orderDetailsList = new ArrayList<>();
            }
            orderDetailsList.add(orderDetails);
        }
        return orderDetailsList;
    }

    @Override
    public OrderDetails getOrderDetailsByOrderId(int orderId) {
        Optional<OrderDetails> orderDetailsHolder = orderDetailsRepository.findById(orderId);
        OrderDetails orderDetails = null;
        if (orderDetailsHolder.isPresent()) {
            orderDetails = orderDetailsHolder.get();
        } else {
            orderDetails = new OrderDetails();
            orderDetails.setError(new Error(OrderBookConstants.OBMS_0003, "Order not found for orderId " + orderId));
        }
        return orderDetails;
    }

    @Override
    public OrderState getOrderStateByOrderId(int orderId) {
        OrderDetails orderDetails = getOrderDetailsByOrderId(orderId);
        if (orderDetails == null) {
            OrderState orderStatus = new OrderState();
            orderStatus.setError(new Error(OrderBookConstants.OBMS_0003, "Order not found for orderId " + orderId));
            return orderStatus;
        }
        return new OrderState(orderDetails.getOrderId(), orderDetails.getOrder().getPrice(),
                orderDetails.getAllocatedQuantity(), orderDetails.getExecutionPrice(), orderDetails.getOrderStatus());
    }

}