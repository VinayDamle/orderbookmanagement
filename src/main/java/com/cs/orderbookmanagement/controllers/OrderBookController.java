package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
import com.cs.orderbookmanagement.utils.JSONMapper;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/orderbook")
public class OrderBookController {

    private JSONMapper mapper;

    @Autowired
    private OrderBookService orderBookService;

    @PostMapping(value = "/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully opened/closed the order book."),
            @ApiResponse(code = 400, message = "Authorization error"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<OrderBookStatusResponse> openOrderBook(@PathVariable int instrumentId) {
        Error error = null;
        HttpStatus openOrCloseOrderBookHttpStatus = HttpStatus.OK;
        OrderBookStatusResponse orderBookStatusResponse = new OrderBookStatusResponse();
        String orderBookStatus = orderBookService.changeOrderBookStatus(instrumentId, OrderBookConstants.OPEN);
        orderBookStatusResponse.setOrderBookStatus(orderBookStatus);
        if (orderBookStatus.equalsIgnoreCase(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND)) {
            openOrCloseOrderBookHttpStatus = HttpStatus.NOT_FOUND;
            error = new Error(OrderBookConstants.OBMS_0001, orderBookStatus);
            orderBookStatusResponse.setError(error);
            orderBookStatusResponse.setOrderBookStatus(null);
        }
        orderBookStatusResponse.setError(error);
        return new ResponseEntity<>(orderBookStatusResponse, openOrCloseOrderBookHttpStatus);
    }

    @PutMapping(value = "/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully opened/closed the order book."),
            @ApiResponse(code = 400, message = "Authorization error"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<OrderBookStatusResponse> closeOrderBook(
            @PathVariable int instrumentId) {
        Error error = null;
        HttpStatus openOrCloseOrderBookHttpStatus = HttpStatus.OK;
        OrderBookStatusResponse orderBookStatusResponse = new OrderBookStatusResponse();
        String orderBookStatus = orderBookService.changeOrderBookStatus(instrumentId, OrderBookConstants.CLOSE);
        orderBookStatusResponse.setOrderBookStatus(orderBookStatus);
        if (orderBookStatus.equalsIgnoreCase(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND)) {
            openOrCloseOrderBookHttpStatus = HttpStatus.NOT_FOUND;
            error = new Error(OrderBookConstants.OBMS_0001, orderBookStatus);
            orderBookStatusResponse.setError(error);
            orderBookStatusResponse.setOrderBookStatus(null);
        }
        orderBookStatusResponse.setError(error);
        return new ResponseEntity<>(orderBookStatusResponse, openOrCloseOrderBookHttpStatus);
    }

    @PostMapping(value = "/{instrumentId}/order", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully added the orders to the order book."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<OrderResponse> addOrder(@PathVariable int instrumentId, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = null;
        HttpStatus addOrderHttpStatus = HttpStatus.OK;
        if (instrumentId != orderRequest.getInstrumentId()) {
            orderResponse = new OrderResponse();
            addOrderHttpStatus = HttpStatus.BAD_REQUEST;
            orderResponse.setOrder(null);
            orderResponse.setError(new Error(OrderBookConstants.OBMS_0004, OrderBookConstants.UNEQUAL_INST_ID));
        } else {
            orderResponse = orderBookService.addOrder(orderRequest, instrumentId);
            if (orderResponse.getError() != null) {
                addOrderHttpStatus = HttpStatus.NOT_FOUND;
            }
        }
        return new ResponseEntity<>(orderResponse, addOrderHttpStatus);
    }

    @PostMapping(value = "/{instrumentId}/execute", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully executed the orders."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<ExecuteOrderResponse> addExecutionAndExecuteOrder(
            @PathVariable int instrumentId, @RequestBody ExecuteOrderRequest executionRequest) {
        ExecuteOrderResponse executedOrderResponse;
        HttpStatus addExecutionAndExecuteOrderHttpStatus = HttpStatus.OK;
        executedOrderResponse = orderBookService.addExecutionAndProcessOrder(executionRequest, instrumentId);
        if (executedOrderResponse.getError() != null) {
            addExecutionAndExecuteOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(executedOrderResponse, addExecutionAndExecuteOrderHttpStatus);
    }

}
