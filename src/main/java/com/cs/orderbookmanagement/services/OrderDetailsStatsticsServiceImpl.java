package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.OrderDetails;
import com.cs.orderbookmanagement.repository.OrderBookRepository;
import com.cs.orderbookmanagement.repository.OrderDetailsRepository;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.cs.orderbookmanagement.utils.OrderBookHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cs.orderbookmanagement.models.Error;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList = orderDetailsRepository.findAll();
        return orderDetailsList;
    }

    @Override
    public List<OrderDetails> getOrderDetailsByInstId(int instrumentId) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList = orderDetailsRepository.findAllOrderDetailsByOrderInstrumentId(instrumentId);
        return orderDetailsList;
    }

    @Override
    public OrderDetails getOrderDetailsByOrderId(int orderId) {
        OrderDetails orderDetails = new OrderDetails();
        Optional<OrderDetails> orderDetailsHolder = orderDetailsRepository.findById(orderId);
        if (!orderDetailsHolder.isPresent()) {
            orderDetails = new OrderDetails();
            orderDetails.setError(new Error (OrderBookConstants.OBMS_0003, "Order not found for orderId " + orderId));
            return orderDetails;
        }
        orderDetails = orderDetailsHolder.get();
        return orderDetails;
    }


}
