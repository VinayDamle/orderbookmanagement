package com.cs.orderbookmanagement.utils;

public interface OrderBookConstants {

    String OPEN = "Open";
    String CLOSE = "Close";
    String UTF_8 = "UTF-8";
    String VALID = "Valid";
    String INVALID = "InValid";
    String ACCEPT = "Accept";
    String REQUEST_URL = "RequestUrl";
    String CONTENT_TYPE = "Content-Type";
    String BASE_URL = "http://localhost:";
    String LIMIT_ORDER = "LimitOrder";
    String MARKET_ORDER = "MarketOrder";
    String AUTHORIZATION = "Authorization";
    String INSTRUMENT_ID_NOT_FOUND = "InstrumentId Not Found.";
    String ERROR_IN_ADDING_INSTRUMENT = "Error in adding instrument.";
    String OBMS_ORD_BOOK_CLOSED_INSTID = "Order book is closed for instrumentid";
    String BASIC_CREDENTIALS = "Basic T3JkZXJCb29rVXNlcjAxOk9yZGVyQm9va1VzZXIwMQ==";
    String INVALID_COMMAND = "Invalid command. Only OPEN & CLOSE are valid command types.";


    String OBMS_0001 = "OBMS_0001";
    String OBMS_0006 = "OBMS_0006";
    String OBMS_0002 = "OBMS_0002";
    String OBMS_0003 = "OBMS_0003";
    String OBMS_0004 = "OBMS_0004";
    String UNEQUAL_INST_ID = "Payload instrumentId is not same as path param instrumentId. Please pass same instrumentId.";
}
