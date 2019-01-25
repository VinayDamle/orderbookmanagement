package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
import com.cs.orderbookmanagement.utils.JSONMapper;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
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

    @PostMapping(value = "/status/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    /*@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully opened/closed the order book."),
            @ApiResponse(code = 400, message = "Authorization error"),
            @ApiResponse(code = 500, message = "Internal server error")})*/
    public ResponseEntity<OrderBookStatusResponse> openOrCloseOrderBook(
            @PathVariable int instrumentId,
            @RequestBody OrderBookStatusCommandRequest orderBookStatusCommandRequest) throws Exception {
        //log.info("The incoming request for instrumentId " + instrumentId + " is " + mapper.serialize(orderBookStatusCommandRequest));
        Error error = null;
        HttpStatus openOrCloseOrderBookHttpStatus = HttpStatus.OK;
        OrderBookStatusResponse orderBookStatusResponse = new OrderBookStatusResponse();
        String orderBookStatus = orderBookService.changeOrderBookStatus(instrumentId, orderBookStatusCommandRequest.getOrderBookStatusCommand());
        orderBookStatusResponse.setOrderBookStatus(orderBookStatus);
        if (orderBookStatus == null) {
            openOrCloseOrderBookHttpStatus = HttpStatus.BAD_REQUEST;
            error = new Error(OrderBookConstants.OBMS_INVL_CMD, OrderBookConstants.INVALID_COMMAND);
            orderBookStatusResponse.setError(new Error(OrderBookConstants.OBMS_INVL_CMD, OrderBookConstants.INVALID_COMMAND));
            orderBookStatusResponse.setOrderBookStatus(null);
        } else {
            if (orderBookStatus.equalsIgnoreCase(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND)) {
                openOrCloseOrderBookHttpStatus = HttpStatus.NOT_FOUND;
                error = new Error(OrderBookConstants.OBMS_INVL_INST_ID, orderBookStatus);
                orderBookStatusResponse.setError(error);
                orderBookStatusResponse.setOrderBookStatus(null);
            }
        }
        orderBookStatusResponse.setError(error);
        //log.info("Service response is " + mapper.serialize(orderBookStatusResponse));
        ResponseEntity<OrderBookStatusResponse> entity = new ResponseEntity<OrderBookStatusResponse>(orderBookStatusResponse, openOrCloseOrderBookHttpStatus);
        return entity;
    }

    @PostMapping(value = "/order/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    /*@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully added the orders to the order book."),
            @ApiResponse(code = 500, message = "Internal server error.")})*/
    public ResponseEntity<OrderDetails> addOrder(@PathVariable int instrumentId, @RequestBody Order order) throws Exception {
        //log.info("The incoming request for instrumentId " + instrumentId + " is " + mapper.serialize(order));
        OrderDetails orderDetails = null;
        HttpStatus addOrderHttpStatus = HttpStatus.OK;
        orderDetails = orderBookService.addOrderToOrderBook(order, instrumentId);
        if (orderDetails.getError() != null) {
            addOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        //log.info("Service response is " + mapper.serialize(orderDetails));
        ResponseEntity<OrderDetails> entity = new ResponseEntity<OrderDetails>(orderDetails, addOrderHttpStatus);
        return entity;
    }

    @PostMapping(value = "/execute/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    /*@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully executed the orders."),
            @ApiResponse(code = 500, message = "Internal server error.")})*/
    public ResponseEntity<ExecutedOrderResponse> addExecutionAndExecuteOrder(
            @PathVariable int instrumentId, @RequestBody InstrumentRequest executionRequest) throws Exception {
        //log.info("The incoming request for instrumentId " + instrumentId + " is " + mapper.serialize(executionRequest));
        ExecutedOrderResponse executedOrderResponse = null;
        HttpStatus addExecutionAndExecuteOrderHttpStatus = HttpStatus.OK;
        executedOrderResponse = orderBookService.addExecutionAndProcessOrder(executionRequest, instrumentId);
        if (executedOrderResponse.getError() != null) {
            addExecutionAndExecuteOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        //log.info("Service response is " + mapper.serialize(executedOrderResponse));
        ResponseEntity<ExecutedOrderResponse> entity = new ResponseEntity<>(executedOrderResponse, addExecutionAndExecuteOrderHttpStatus);
        return entity;
    }

}
