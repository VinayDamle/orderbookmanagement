package com.cs.orderbookmanagement.controllers;

import com.cs.orderbookmanagement.models.Error;
import com.cs.orderbookmanagement.models.*;
import com.cs.orderbookmanagement.services.OrderBookService;
import com.cs.orderbookmanagement.utils.JSONMapper;
import com.cs.orderbookmanagement.utils.OrderBookConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully opened/closed the order book."),
            @ApiResponse(code = 400, message = "Authorization error"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<OBStatusResponse> openOrderBook() {
        Error error = null;
        HttpStatus openOrCloseOrderBookHttpStatus = HttpStatus.OK;
        OBStatusResponse orderBookStatusResponse = new OBStatusResponse();
        Long orderBookId = orderBookService.openOrderBook();
        if (orderBookId == null) {
            openOrCloseOrderBookHttpStatus = HttpStatus.NOT_FOUND;
            orderBookStatusResponse.setError(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        }
        orderBookStatusResponse.setOrderBookId(orderBookId);
        orderBookStatusResponse.setBookStatus(OrderBookConstants.OPEN);
        return new ResponseEntity<>(orderBookStatusResponse, openOrCloseOrderBookHttpStatus);
    }

    @PutMapping(value = "/{orderBookId}", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully opened/closed the order book."),
            @ApiResponse(code = 400, message = "Authorization error"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<OBStatusResponse> closeOrderBook(@PathVariable Long orderBookId) {
        Error error = null;
        OBStatusResponse orderBookStatusResponse = new OBStatusResponse();
        HttpStatus closeOrderBookHttpStatus = HttpStatus.OK;
        String orderBookStatus = orderBookService.closeOrderBook(orderBookId);
        if (orderBookStatus.equalsIgnoreCase(OrderBookConstants.INSTRUMENT_ID_NOT_FOUND)) {
            closeOrderBookHttpStatus = HttpStatus.NOT_FOUND;
            orderBookStatusResponse.setError(new Error(OrderBookConstants.OBMS_0001, OrderBookConstants.INSTRUMENT_ID_NOT_FOUND));
        }
        orderBookStatusResponse.setBookStatus(orderBookStatus);
        return new ResponseEntity<>(orderBookStatusResponse, closeOrderBookHttpStatus);
    }

    @PostMapping(value = "/{instrumentId}/order", produces = {"application/json"}, consumes = {"application/json"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully added the orders to the order book."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    public ResponseEntity<OrderResponse> addOrder(@PathVariable Long instrumentId,
                                                  @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse;
        HttpStatus addOrderHttpStatus = HttpStatus.OK;
        if (instrumentId.longValue() != orderRequest.getInstrumentId().longValue()) {
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
            @PathVariable Long instrumentId, @RequestBody ExecuteOrderRequest executionRequest) {
        ExecuteOrderResponse executedOrderResponse;
        HttpStatus addExecutionAndExecuteOrderHttpStatus = HttpStatus.OK;
        executedOrderResponse = orderBookService.addExecutionAndProcessOrder(executionRequest, instrumentId);
        if (executedOrderResponse.getError() != null) {
            addExecutionAndExecuteOrderHttpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(executedOrderResponse, addExecutionAndExecuteOrderHttpStatus);
    }

}
