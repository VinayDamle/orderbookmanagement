package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/orderbookmanagement")
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @PostMapping(value = "/status/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully opened/closed the order book."),
            @ApiResponse(code = 400, message = "Authorization error"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<OrderBookStatusResponse> openOrCloseOrderBook(
            @PathVariable int instrumentId,
            @RequestBody OrderBookStatusCommandRequest orderBookStatusCommandRequest) {
        Error error = null;
        HttpStatus openOrCloseOrderBookHttpStatus = HttpStatus.OK;
        OrderBookStatusResponse orderBookStatusResponse = new OrderBookStatusResponse();
        String orderBookStatus = orderBookService.changeOrderBookStatus(instrumentId, orderBookStatusCommandRequest.getOrderBookStatusCommand());
        orderBookStatusResponse.setOrderBookStatus(orderBookStatus);
        if (orderBookStatus == null) {
            openOrCloseOrderBookHttpStatus = HttpStatus.BAD_REQUEST;
            error = new Error("", "Invalid command. Only OPEN & CLOSE are valid command types.");
            orderBookStatusResponse.setError(new Error("", "Invalid command. Only OPEN & CLOSE are valid command types."));
            orderBookStatusResponse.setOrderBookStatus(null);
        } else {
            if (orderBookStatus.equalsIgnoreCase(OrderBookService.INSTRUMENT_ID_NOT_FOUND)) {
                openOrCloseOrderBookHttpStatus = HttpStatus.NOT_FOUND;
                error = new Error("", orderBookStatus);
                orderBookStatusResponse.setError(error);
                orderBookStatusResponse.setOrderBookStatus(null);
            }
        }
        orderBookStatusResponse.setError(error);
        ResponseEntity<OrderBookStatusResponse> entity = new ResponseEntity<OrderBookStatusResponse>(orderBookStatusResponse, openOrCloseOrderBookHttpStatus);
        return entity;
    }

    @PostMapping(value = "/order/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully added the orders to the order book."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<OrderDetails> addOrder(@PathVariable int instrumentId, @RequestBody Order order) {
        OrderDetails orderDetails = null;
        HttpStatus addOrderHttpStatus = HttpStatus.OK;
        orderDetails = orderBookService.addOrder(order, instrumentId);
        if (orderDetails.getError() != null) {
            addOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        ResponseEntity<OrderDetails> entity = new ResponseEntity<OrderDetails>(orderDetails, addOrderHttpStatus);
        return entity;
    }

    @PostMapping(value = "/execute/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully executed the orders."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<ExecutedOrderResponse> addExecutionAndExecuteOrder(@PathVariable int instrumentId, @RequestBody ExecutionRequest executionRequest) {
        ExecutedOrderResponse executedOrderResponse = null;
        HttpStatus addExecutionAndExecuteOrderHttpStatus = HttpStatus.OK;
        executedOrderResponse = orderBookService.addExecutionAndProcessOrder(executionRequest, instrumentId);
        if (executedOrderResponse.getError() != null) {
            addExecutionAndExecuteOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        ResponseEntity<ExecutedOrderResponse> entity = new ResponseEntity<>(executedOrderResponse, addExecutionAndExecuteOrderHttpStatus);
        return entity;
    }

}
