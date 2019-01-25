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
        String command = orderBookStatusCommandRequest.getOrderBookStatusCommand();
        if (command == null || !((command.equalsIgnoreCase(OrderBookConstants.OPEN) || command.equalsIgnoreCase(OrderBookConstants.CLOSE)))) {
            openOrCloseOrderBookHttpStatus = HttpStatus.BAD_REQUEST;
            error = new Error(OrderBookConstants.OBMS_0002, OrderBookConstants.INVALID_COMMAND);
            orderBookStatusResponse.setError(new Error(OrderBookConstants.OBMS_0002, OrderBookConstants.INVALID_COMMAND));
            orderBookStatusResponse.setOrderBookStatus(null);
        } else {
            String orderBookStatus = orderBookService.changeOrderBookStatus(instrumentId, orderBookStatusCommandRequest.getOrderBookStatusCommand());
            orderBookStatusResponse.setOrderBookStatus(orderBookStatus);
            if (orderBookStatus.equalsIgnoreCase(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND)) {
                openOrCloseOrderBookHttpStatus = HttpStatus.NOT_FOUND;
                error = new Error(OrderBookConstants.OBMS_0001, orderBookStatus);
                orderBookStatusResponse.setError(error);
                orderBookStatusResponse.setOrderBookStatus(null);
            }
        }
        orderBookStatusResponse.setError(error);
        //log.info("Service response is " + mapper.serialize(orderBookStatusResponse));
        return new ResponseEntity<OrderBookStatusResponse>(orderBookStatusResponse, openOrCloseOrderBookHttpStatus);
    }

    @PostMapping(value = "/order/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    /*@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully added the orders to the order book."),
            @ApiResponse(code = 500, message = "Internal server error.")})*/
    public ResponseEntity<OrderDetails> addOrder(@PathVariable int instrumentId, @RequestBody Order order) throws Exception {
        //log.info("The incoming request for instrumentId " + instrumentId + " is " + mapper.serialize(order));
        OrderDetails orderDetails;
        HttpStatus addOrderHttpStatus = HttpStatus.OK;
        if (instrumentId != order.getInstrumentId()) {
            orderDetails = new OrderDetails();
            addOrderHttpStatus = HttpStatus.BAD_REQUEST;
            orderDetails.setOrder(null);
            orderDetails.setError(new Error(OrderBookConstants.OBMS_0004, OrderBookConstants.UNEQUAL_INST_ID));
        } else {
            orderDetails = orderBookService.addOrder(order, instrumentId);
            if (orderDetails.getError() != null) {
                addOrderHttpStatus = HttpStatus.NOT_FOUND;
            }
        }
        //log.info("Service response is " + mapper.serialize(orderDetails));
        return new ResponseEntity<OrderDetails>(orderDetails, addOrderHttpStatus);
    }

    @PostMapping(value = "/execute/{instrumentId}", produces = {"application/json"}, consumes = {"application/json"})
    /*@ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully executed the orders."),
            @ApiResponse(code = 500, message = "Internal server error.")})*/
    public ResponseEntity<ExecutedOrderResponse> addExecutionAndExecuteOrder(
            @PathVariable int instrumentId, @RequestBody ExecutionRequest executionRequest) throws Exception {
        //log.info("The incoming request for instrumentId " + instrumentId + " is " + mapper.serialize(executionRequest));
        ExecutedOrderResponse executedOrderResponse;
        HttpStatus addExecutionAndExecuteOrderHttpStatus = HttpStatus.OK;
        executedOrderResponse = orderBookService.addExecutionAndProcessOrder(executionRequest, instrumentId);
        if (executedOrderResponse.getError() != null) {
            addExecutionAndExecuteOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        //log.info("Service response is " + mapper.serialize(executedOrderResponse));
        return new ResponseEntity<>(executedOrderResponse, addExecutionAndExecuteOrderHttpStatus);
    }

}
