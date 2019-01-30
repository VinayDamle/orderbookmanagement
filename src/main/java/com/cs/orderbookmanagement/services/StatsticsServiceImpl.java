package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.entities.OrderDao;
import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.repository.OrderBookRepository;
import com.cs.orderbookmanagement.repository.OrderDetailsRepository;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class StatsticsServiceImpl implements StatsticsService {

    @Autowired
    private OrderBookHelper helper;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<OrderDetail> orderDetails = orderDetailsRepository.findAll();
        orderDetails.stream().forEach(vo -> {
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
        return orderResponses;
    }

    public OrderStatstics getOrderStatstics(Boolean fetchValidInvalidRecords) {
        OrderStatstics osc = new OrderStatstics();
        List<OrderDetail> ods = orderDetailsRepository.findAll();
        if (ods == null || ods.isEmpty()) {
            osc.setError(new Error("", "No order found."));
            return osc;
        }
        osc = new OrderStatstics();
        osc.setTotalNoOfOrders(ods.size());
        sortByOrderByQuantityAsc(ods);
        OrderDao orderDao = ods.get(0).getOrder();
        OrderRequest biggestOrder = new OrderRequest(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setBiggestOrder(biggestOrder);
        orderDao = ods.get(ods.size() - 1).getOrder();
        OrderRequest smallestOrder = new OrderRequest(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setSmallestOrder(smallestOrder);
        sortByOrderByEntryDateAsc(ods);
        orderDao = ods.get(0).getOrder();
        OrderRequest earliestOrder = new OrderRequest(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setEarliestOrderEntry(earliestOrder);
        orderDao = ods.get(ods.size() - 1).getOrder();
        OrderRequest latestOrder = new OrderRequest(orderDao.getQuantity(), orderDao.getEntryDate(), orderDao.getInstrumentId(), orderDao.getPrice());
        osc.setLatestOrderEntry(latestOrder);
        if (fetchValidInvalidRecords) {
            setAdditionalRecords(osc, ods);
        }
        return osc;
    }

    private void setAdditionalRecords(OrderStatstics osc, List<OrderDetail> ods) {
        Integer validOrderCount = null;
        Integer invalidOrderCount = null;
        for (OrderDetail od : ods) {
            if (od.getOrderStatus() != null) {
                if (OrderBookConstants.VALID.equalsIgnoreCase(od.getOrderStatus())) {
                    if (validOrderCount == null) {
                        validOrderCount = 0;
                    }
                    validOrderCount++;
                }
                if (OrderBookConstants.INVALID.equalsIgnoreCase(od.getOrderStatus())) {
                    if (invalidOrderCount == null) {
                        invalidOrderCount = 0;
                    }
                    invalidOrderCount++;
                }
            }
        }
        osc.setValidOrders(validOrderCount);
        osc.setInvalidOrders(invalidOrderCount);
    }


    private void sortByOrderByQuantityAsc(List<OrderDetail> unSortedOrderDetails) {
        Collections.sort(unSortedOrderDetails, (o1, o2) -> o2.getOrder().getQuantity() - o1.getOrder().getQuantity());
    }

    private void sortByOrderByEntryDateAsc(List<OrderDetail> unSortedOrderDetails) {
        Collections.sort(unSortedOrderDetails, new Comparator<OrderDetail>() {
            @Override
            public int compare(OrderDetail o1, OrderDetail o2) {
                return o1.getOrder().getEntryDate().compareTo(o2.getOrder().getEntryDate());
            }
        });
    }

    @Override
    public List<OrderResponse> getOrderDetailsByInstId(Long instrumentId) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<OrderDetail> orderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(instrumentId);
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            OrderResponse or = new OrderResponse();
            or.setError(new Error(OrderBookConstants.OBMS_0003, "Order not found for instrumentId " + instrumentId));
            orderResponses.add(or);
        } else {
            orderDetailsList.stream().forEach(vo -> {
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
        }
        return orderResponses;
    }

    @Override
    public OrderResponse getOrderDetailsByOrderId(Long orderId) {
        OrderResponse orderResponse;
        Optional<OrderDetail> orderDetailsHolder = orderDetailsRepository.findById(orderId);
        OrderDetail orderDetails;
        if (orderDetailsHolder.isPresent()) {
            orderDetails = orderDetailsHolder.get();
            orderResponse = new OrderResponse();
            orderResponse.setOrder(new OrderRequest(orderDetails.getOrder().getQuantity(), orderDetails.getOrder().getEntryDate(),
                    orderDetails.getOrder().getInstrumentId(), orderDetails.getOrder().getPrice()));
            orderResponse.setOrderStatus(orderDetails.getOrderStatus());
            orderResponse.setOrderType(orderDetails.getOrderType());
            orderResponse.setAllocatedQuantity(orderDetails.getAllocatedQuantity());
            orderResponse.setExecutionPrice(orderDetails.getExecutionPrice());
        } else {
            orderResponse = new OrderResponse();
            orderResponse.setError(new Error(OrderBookConstants.OBMS_0003, "Order not found for orderId " + orderId));
        }
        return orderResponse;
    }

    @Override
    public OrderState getOrderStateByOrderId(Long orderId) {
        OrderResponse orderDetails = getOrderDetailsByOrderId(orderId);
        if (orderDetails == null) {
            OrderState orderStatus = new OrderState();
            orderStatus.setError(new Error(OrderBookConstants.OBMS_0003, "Order not found for orderId " + orderId));
            return orderStatus;
        }
        return new OrderState(orderId, orderDetails.getOrder().getPrice(),
                orderDetails.getAllocatedQuantity(), orderDetails.getExecutionPrice(), orderDetails.getOrderStatus());
    }

}