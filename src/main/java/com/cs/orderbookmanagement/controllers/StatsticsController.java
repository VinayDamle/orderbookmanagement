package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.entities.OrderDetail;
import com.cs.orderbookmanagement.models.OrderState;
import com.cs.orderbookmanagement.models.OrderStatstics;
import com.cs.orderbookmanagement.services.StatsticsService;
import com.cs.orderbookmanagement.utils.JSONMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/orderbook/statstics")
public class StatsticsController {

    private JSONMapper mapper;

    @Autowired
    private StatsticsService orderDetailsStatsticsService;

    @GetMapping(value = "/orders", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<List<OrderDetail>> getAllOrders() {
        HttpStatus getAllOrdersHttpStatus = HttpStatus.OK;
        List<OrderDetail> orderDetailsList = orderDetailsStatsticsService.getAllOrders();
        return new ResponseEntity<>(orderDetailsList, getAllOrdersHttpStatus);
    }

    @GetMapping(produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderStatstics> getOrderStatstics(@RequestParam(value = "fetchValidInvalidRecords",
            required = false, defaultValue = "false") boolean fetchValidInvalidRecords) {
        HttpStatus getOrderStatsticsHttpStatus = HttpStatus.OK;
        OrderStatstics orderStatstics = orderDetailsStatsticsService.getOrderStatstics(fetchValidInvalidRecords);
        if (orderStatstics.getError() != null) {
            getOrderStatsticsHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderStatstics, getOrderStatsticsHttpStatus);
    }

    @GetMapping(value = "/{instrumentId}/orders", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByInstId(@PathVariable int instrumentId) {
        HttpStatus orderDetailsByInstIdHttpStatus = HttpStatus.OK;
        List<OrderDetail> orderDetailsList = orderDetailsStatsticsService.getOrderDetailsByInstId(instrumentId);
        if (!orderDetailsList.isEmpty() && orderDetailsList.get(0) != null && orderDetailsList.get(0).getError() != null) {
            orderDetailsByInstIdHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderDetailsList, orderDetailsByInstIdHttpStatus);
    }

    @GetMapping(value = "/order/{orderId}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderDetail> getOrderDetailsByOrderId(@PathVariable int orderId) {
        HttpStatus orderDetailsByOrderIdHttpStatus = HttpStatus.OK;
        OrderDetail orderDetails = orderDetailsStatsticsService.getOrderDetailsByOrderId(orderId);
        if (orderDetails.getError() != null) {
            orderDetailsByOrderIdHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderDetails, orderDetailsByOrderIdHttpStatus);
    }

    @GetMapping(value = "/order/{orderId}/orderState", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<OrderState> getOrderStateByOrderId(@PathVariable int orderId) {
        HttpStatus orderStateByOrderIdHttpStatus = HttpStatus.OK;
        OrderState orderState = orderDetailsStatsticsService.getOrderStateByOrderId(orderId);
        if (orderState.getError() != null) {
            orderStateByOrderIdHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderState, orderStateByOrderIdHttpStatus);
    }

}
