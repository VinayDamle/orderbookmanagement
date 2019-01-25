package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.OrderDetails;
import com.cs.orderbookmanagement.services.OrderDetailsStatsticsService;
import com.cs.orderbookmanagement.utils.JSONMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/orderbook/statstics")
public class OrderDetailsStatsticsController {

    private JSONMapper mapper;

    @Autowired
    private OrderDetailsStatsticsService orderDetailsStatsticsService;

    @GetMapping(value = "/orders", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<List<OrderDetails>> getAllOrders() throws Exception {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        HttpStatus addOrderHttpStatus = HttpStatus.OK;
        orderDetailsList = orderDetailsStatsticsService.getAllOrders();
        ResponseEntity<List<OrderDetails>> entity = new ResponseEntity<List<OrderDetails>>(orderDetailsList, addOrderHttpStatus);
        return entity;
    }

    @GetMapping(value = "/{instrumentId}/orders", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<List<OrderDetails>> getOrderDetailsByInstId(@PathVariable int instrumentId) throws Exception {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        HttpStatus orderDetailsByInstIdHttpStatus = HttpStatus.OK;
        orderDetailsList = orderDetailsStatsticsService.getOrderDetailsByInstId(instrumentId);
        ResponseEntity<List<OrderDetails>> entity = new ResponseEntity<List<OrderDetails>>(orderDetailsList, orderDetailsByInstIdHttpStatus);
        return entity;
    }

    @GetMapping(value = "/order/{orderId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderDetails> getOrderDetailsByOrderId(@PathVariable int orderId) throws Exception {
        OrderDetails orderDetails = null;
        HttpStatus orderDetailsByOrderIdHttpStatus = HttpStatus.OK;
        orderDetails = orderDetailsStatsticsService.getOrderDetailsByOrderId(orderId);
        ResponseEntity<OrderDetails> entity = new ResponseEntity<OrderDetails>(orderDetails, orderDetailsByOrderIdHttpStatus);
        return entity;
    }

}
