package com.cs.orderbookmanagement.utils;

public interface OrderBookConstants {

    static final String JSON = "json";
    static final String OPEN = "Open";
    static final String ORDER = "order/";
    static final String CLOSE = "Close";
    static final String UTF_8 = "UTF-8";
    static final String VALID = "Valid";
    static final String STATUS = "status/";
    static final String INVALID = "InValid";
    static final String EXECUTE = "execute/";
    static final String ACCEPT = "Accept";
    static final String REQUEST_URL = "RequestUrl";
    static final String CONTENT_TYPE = "Content-Type";
    static final String BASE_URL = "http://localhost:";
    static final String LIMIT_ORDER = "LimitOrder";
    static final String MARKET_ORDER = "MarketOrder";
    static final String ORDERBOOKMGMNT = "/orderbook/";
    static final String AUTHORIZATION = "Authorization";
    static final String INSTRUMENT_ID_NOT_FOUND = "InstrumentId Not Found.";
    static final String ERROR_IN_ADDING_INSTRUMENT = "Error in adding instrument.";
    static final String OBMS_ORD_BOOK_CLOSED_INSTID = "Order book is closed for instrumentid";
    static final String BASIC_CREDENTIALS = "Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==";
    static final String INVALID_COMMAND = "Invalid command. Only OPEN & CLOSE are valid command types.";


    static final String OBMS_0001 = "OBMS_0001";
    static final String OBMS_0006 = "OBMS_0006";
    static final String OBMS_0002 = "OBMS_0002";
    static final String OBMS_0003 = "OBMS_0003";
    static final String OBMS_0004 = "OBMS_0004";
    static final String UNEQUAL_INST_ID = "Payload instrumentId is not same as path param instrumentId. Please pass same instrumentId.";
}
