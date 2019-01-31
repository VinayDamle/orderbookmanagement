package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.OrderResponse;
import com.cs.orderbookmanagement.models.OrderState;
import com.cs.orderbookmanagement.models.OrderStatstics;
import com.cs.orderbookmanagement.services.StatsticsService;
import com.cs.orderbookmanagement.utils.JSONMapper;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully gets the all order details including"),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        HttpStatus getAllOrdersHttpStatus = HttpStatus.OK;
        List<OrderResponse> orderDetailsList = orderDetailsStatsticsService.getAllOrders();
        return new ResponseEntity<>(orderDetailsList, getAllOrdersHttpStatus);
    }

    @GetMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully gets the all order details including " +
            "total number of orders, valid and invalid order count"),
            @ApiResponse(code = 500, message = "Internal server error.")})
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully gets the order details by given instrumentId."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<List<OrderResponse>> getOrderDetailsByInstId(@PathVariable Long instrumentId) {
        HttpStatus orderDetailsByInstIdHttpStatus = HttpStatus.OK;
        List<OrderResponse> orderDetailsList = orderDetailsStatsticsService.getOrderDetailsByInstId(instrumentId);
        if (!orderDetailsList.isEmpty() && orderDetailsList.get(0) != null && orderDetailsList.get(0).getError() != null) {
            orderDetailsByInstIdHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderDetailsList, orderDetailsByInstIdHttpStatus);
    }

    @GetMapping(value = "/order/{orderId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully gets the order details by given orderId."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<OrderResponse> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        HttpStatus orderDetailsByOrderIdHttpStatus = HttpStatus.OK;
        OrderResponse orderDetails = orderDetailsStatsticsService.getOrderDetailsByOrderId(orderId);
        if (orderDetails.getError() != null) {
            orderDetailsByOrderIdHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderDetails, orderDetailsByOrderIdHttpStatus);
    }

    @GetMapping(value = "/order/{orderId}/orderState", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully gets the order state of an order by given orderId."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<OrderState> getOrderStateByOrderId(@PathVariable Long orderId) {
        HttpStatus orderStateByOrderIdHttpStatus = HttpStatus.OK;
        OrderState orderState = orderDetailsStatsticsService.getOrderStateByOrderId(orderId);
        if (orderState.getError() != null) {
            orderStateByOrderIdHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(orderState, orderStateByOrderIdHttpStatus);
    }

}
