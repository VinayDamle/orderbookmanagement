package com.cs.orderbookmanagement.services;

import com.cs.orderbookmanagement.models.*;

public interface OrderBookService {

    static final String OPEN = "Open";
    static final String UTF_8 = "UTF-8";
    static final String VALID = "Valid";
    static final String OPENED = "Opened";
    static final String CLOSED = "Closed";
    String OBMS_INVL_001 = "OBMS-INVL-001";
    static final String LIMIT_ORDER = "LimitOrder";
    static final String MARKET_ORDER = "MarketOrder";
    String INSTRUMENT_ID_NOT_FOUND = "InstrumentId Not Found.";
    static final String ORDERBOOKMGMNT = "/orderbookmanagement/";

    public OrderDetails addOrder(Order order, int instrumentId);

    public String changeOrderBookStatus(int instrumentId, String command);

    public ExecutedOrderResponse addExecutionAndProcessOrder(ExecutionRequest execution, int instrumentId);
}
